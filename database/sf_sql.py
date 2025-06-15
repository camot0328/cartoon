
import requests
import time
import html
from urllib.parse import quote
from datetime import datetime

# 🔧 설정값
API_KEY = "ttbcarrot_092209001"
QUERY = "SF"
MAX_RESULTS = 100
TOTAL_PAGES = 5  # 100 × 5 = 최대 500권
OUTPUT_FILE = "sf_cartoon_data.sql"

def fetch_books(query, page):
    url = f"https://www.aladin.co.kr/ttb/api/ItemSearch.aspx"
    params = {
        "ttbkey": API_KEY,
        "Query": query,
        "QueryType": "Keyword",
        "MaxResults": MAX_RESULTS,
        "start": 1 + (page - 1) * MAX_RESULTS,
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
    time.sleep(1)  # 과도한 요청 방지

print(f"✅ 총 수집된 책 수: {len(books)}")

# 장르 ID 매핑
genre_map = {}
genre_id = 1
product_id = 1
category_sql = set()
product_sql = []
product_category_sql = []
stock_sql = []

for book in books:
    title = html.escape(book.get("title", "제목 없음")).replace("'", "''")
    author = html.escape(book.get("author", "작자 미상")).replace("'", "''")
    publisher = html.escape(book.get("publisher", "미상")).replace("'", "''")
    pub_date = book.get("pubDate", "2000-01-01")
    price = int(book.get("priceStandard", 0))
    thumbnail = book.get("cover", "")
    detail_img = thumbnail
    rating = float(book.get("customerReviewRank", 0))

    # INSERT INTO product
    product_sql.append(f"""INSERT INTO product (id, title, author, publisher, publish_date, price, thumbnail_img, detail_img, rating)
VALUES ({product_id}, '{title}', '{author}', '{publisher}', '{pub_date}', {price}, '{thumbnail}', '{detail_img}', {rating});""")

    # INSERT INTO stock
    stock_sql.append(f"INSERT INTO stock (product_id, quantity, state) VALUES ({product_id}, 100, '판매중');")

    # 장르 파싱
    category_name = book.get("categoryName", "")
    raw_genres = category_name.replace("만화 > ", "").split("/") if "만화 > " in category_name else ["SF"]
    for genre in raw_genres:
        genre = genre.strip()
        if genre not in genre_map:
            genre_map[genre] = genre_id
            category_sql.add(f"INSERT INTO category (id, name) VALUES ({genre_id}, '{genre}');")
            genre_id += 1
        cid = genre_map[genre]
        product_category_sql.append(f"INSERT INTO product_category (product_id, category_id) VALUES ({product_id}, {cid});")
    product_id += 1

# 파일 저장
with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
    f.write("-- CATEGORY\n" + "\n".join(sorted(category_sql)) + "\n\n")
    f.write("-- PRODUCT\n" + "\n".join(product_sql) + "\n\n")
    f.write("-- PRODUCT_CATEGORY\n" + "\n".join(product_category_sql) + "\n\n")
    f.write("-- STOCK\n" + "\n".join(stock_sql) + "\n")

print(f"📄 SQL 파일 저장 완료: {OUTPUT_FILE}")
