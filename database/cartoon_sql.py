
import requests
import time
import html
from urllib.parse import quote

# 사용자 설정 부분👇
API_KEY = "ttbcarrot_092209001"
QUERY = "순정만화"  # 장르 키워드
PRODUCT_ID_START = 2751  # product 테이블에 들어갈 시작 ID
OUTPUT_FILE = "순정_cartoon_data.sql"  # 결과 SQL 파일명
TOTAL_PAGES = 5  # 최대 500권 (100권 × 5 페이지)

def fetch_books(query, page):
    url = f"https://www.aladin.co.kr/ttb/api/ItemSearch.aspx"
    params = {
        "ttbkey": API_KEY,
        "Query": query,
        "QueryType": "Keyword",
        "MaxResults": 100,
        "start": 1 + (page - 1) * 100,
        "SearchTarget": "Book",
        "output": "js",
        "Version": "20131101"
    }
    response = requests.get(url, params=params)
    if response.status_code == 200:
        return response.json().get("item", [])
    else:
        print("API 호출 실패:", response.status_code)
        return []

books = []
for page in range(1, TOTAL_PAGES + 1):
    print(f"📦 {page}페이지 수집 중...")
    items = fetch_books(QUERY, page)
    if not items:
        break
    books.extend(items)
    time.sleep(1)

print(f"✅ 총 수집된 책 수: {len(books)}")

# SQL 생성
category_set = set()
product_sql = []
stock_sql = []
product_category_sql = []
product_id = PRODUCT_ID_START

for book in books:
    title = html.escape(book.get("title", "제목 없음")).replace("'", "''")
    author = html.escape(book.get("author", "작자 미상")).replace("'", "''")
    publisher = html.escape(book.get("publisher", "미상")).replace("'", "''")
    pub_date = book.get("pubDate", "2000-01-01")
    price = int(book.get("priceStandard", 0))
    thumbnail = book.get("cover", "")
    detail_img = thumbnail
    rating = float(book.get("customerReviewRank", 0))

    product_sql.append(f"""INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating)
VALUES ({product_id}, '{title}', '{author}', '{publisher}', '{pub_date}', {price}, '{thumbnail}', '{detail_img}', {rating});""")

    stock_sql.append(f"INSERT INTO stock (product_id, quantity, state) VALUES ({product_id}, 100, '판매중');")

    category_name = book.get("categoryName", "")
    raw_genres = category_name.replace("만화 > ", "").split("/") if "만화 > " in category_name else [QUERY]
    for genre in raw_genres:
        genre = genre.strip()
        category_set.add(genre)
        product_category_sql.append(
            f"INSERT INTO product_category (product_id, category_id) SELECT {product_id}, id FROM category WHERE name = '{genre}';"
        )

    product_id += 1

# 카테고리 INSERT (중복 방지용)
category_sql = [f"INSERT IGNORE INTO category (name) VALUES ('{name}');" for name in sorted(category_set)]

# 저장
with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
    f.write("-- CATEGORY\n" + "\n".join(category_sql) + "\n\n")
    f.write("-- PRODUCT\n" + "\n".join(product_sql) + "\n\n")
    f.write("-- PRODUCT_CATEGORY\n" + "\n".join(product_category_sql) + "\n\n")
    f.write("-- STOCK\n" + "\n".join(stock_sql) + "\n")

print(f"📄 SQL 파일 저장 완료: {OUTPUT_FILE}")
