
import requests
import pymysql

# 설정
API_KEY = "ttbcarrot_092209001"
# CATEGORY_ID = 2551  # 알라딘 '만화' 카테고리 ID
CATEGORY_ID = 4673  # 알라딘 '만화' 카테고리 ID
QUERY = "만화"  # 키워드
CATEGORY_NAME = "고양만화"  # 우리가 지정할 카테고리명
TOTAL_PAGES = 5
PRODUCT_ID_START = 4661

# DB 연결
conn = pymysql.connect(
    host='localhost',
    user='root',
    password='1111',  # 실제 비밀번호로 바꿔야 함
    db='cartoon',
    charset='utf8'
)
cursor = conn.cursor()

# 기존 title 확인
cursor.execute("SELECT id, title FROM product;")
existing_products = {title: id for id, title in cursor.fetchall()}

# 카테고리 ID 확인 또는 생성
cursor.execute("SELECT id FROM category WHERE name = %s", (CATEGORY_NAME,))
row = cursor.fetchone()
if row:
    category_id = row[0]
else:
    cursor.execute("INSERT INTO category (name) VALUES (%s)", (CATEGORY_NAME,))
    conn.commit()
    category_id = cursor.lastrowid

product_id = PRODUCT_ID_START
inserted = 0
skipped = 0
failed = 0

for page in range(1, TOTAL_PAGES + 1):
    url = f"http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey={API_KEY}&Query={QUERY}&QueryType=Keyword&MaxResults=100&start={page}&SearchTarget=Book&CategoryId={CATEGORY_ID}&output=js&Version=20131101"
    try:
        res = requests.get(url)
        res.raise_for_status()
        data = res.json()
    except Exception as e:
        print(f"API 요청 실패 (page {page}):", e)
        break

    items = data.get("item", [])
    if not items:
        break

    for item in items:
        try:
            title = item.get("title", "").strip()
            author = item.get("author", "").strip()
            publisher = item.get("publisher", "").strip()
            pub_date = item.get("pubDate", "").strip()
            price = int(item.get("priceStandard", 0))
            thumb = item.get("cover", "").strip()
            rating = float(item.get("customerReviewRank", 0))

            if not title or price == 0:
                continue

            if title in existing_products:
                product_id_used = existing_products[title]
                cursor.execute(
                    "INSERT IGNORE INTO product_category (product_id, category_id) VALUES (%s, %s)",
                    (product_id_used, category_id)
                )
                skipped += 1
            else:
                cursor.execute(
                    "INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating) "
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
                    (product_id, title, author, publisher, pub_date, price, thumb, thumb, rating)
                )
                cursor.execute("INSERT INTO stock (product_id, quantity) VALUES (%s, %s)", (product_id, 100))
                cursor.execute("INSERT INTO product_category (product_id, category_id) VALUES (%s, %s)", (product_id, category_id))
                existing_products[title] = product_id
                product_id += 1
                inserted += 1

        except Exception as e:
            print("삽입 실패:", e)
            failed += 1

    conn.commit()

print(f"완료: 삽입됨: {inserted}, 기존 연결됨: {skipped}, 실패: {failed}")
cursor.close()
conn.close()
