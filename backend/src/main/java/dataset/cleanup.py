import pandas as pd
import os
import re

def clean_source_data(input_csv='BooksDatasetClean.csv', output_csv='cleaned_dataset.csv'):
    print(f"Reading raw data from {input_csv}...")
    try:
        df = pd.read_csv(input_csv)
    except FileNotFoundError:
        print(f"Error: The input file '{input_csv}' was not found.")
        return None

    columns_to_drop = ["Publisher", "Publish Date (Month)", "Price Starting With ($)"]
    df = df.drop(columns=columns_to_drop, axis=1, errors='ignore')

    df = df.dropna(subset=['Category', 'Publish Date (Year)'])

    df['Authors'] = df['Authors'].str.replace(r'^\s*By\s+', '', regex=True, case=False).str.strip()

    columns_to_rename = {
        'Publish Date (Year)': 'Year'
    }
    df = df.rename(columns=columns_to_rename)

    df.to_csv(output_csv, index=False)
    print(f"Cleaned data saved to {output_csv}")
    return df

def generate_sql_files(df):

    print("Starting SQL generation...")

    def split_authors_heuristic(author_string):
        """A more robust way to split author strings."""
        if pd.isna(author_string):
            return []
        author_string = re.sub(r'\s*\([^)]*\)', '', author_string)
        author_string = author_string.replace(' and ', ',')
        parts = [p.strip() for p in author_string.split(',') if p.strip()]
        authors = []
        if len(parts) > 0 and len(parts) % 2 == 0:
            for i in range(0, len(parts), 2):
                authors.append(f"{parts[i]}, {parts[i+1]}")
        else:
            return parts
        return authors

    def split_categories(data_string):
        """Splits category strings."""
        if pd.isna(data_string):
            return []
        return [item.strip() for item in str(data_string).split(',') if item.strip()]

    df['author_list'] = df['Authors'].apply(split_authors_heuristic)
    df['category_list'] = df['Category'].apply(split_categories)

    unique_authors = set(author for authors in df['author_list'] for author in authors)
    unique_categories = set(category for categories in df['category_list'] for category in categories)

    author_to_id = {name: i + 1 for i, name in enumerate(sorted(list(unique_authors)))}
    category_to_id = {name: i + 1 for i, name in enumerate(sorted(list(unique_categories)))}

    print(f"Found {len(unique_authors)} unique authors and {len(unique_categories)} unique categories.")

    def write_batched_sql(file_handle, table_name, columns, values_list, batch_size=200):
        """Writes multi-row INSERT statements in batches."""
        if not values_list:
            return

        for i in range(0, len(values_list), batch_size):
            batch = values_list[i:i + batch_size]
            file_handle.write(f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES\n")
            values_str = ',\n'.join(batch)
            file_handle.write(values_str + ';\n\n')

    author_values = []
    category_values = []
    book_values = []
    book_author_values = []
    book_category_values = []

    for name, author_id in author_to_id.items():
        safe_name = name.replace("'", "''")
        author_values.append(f"({author_id}, '{safe_name}')")

    for name, category_id in category_to_id.items():
        safe_name = name.replace("'", "''")
        category_values.append(f"({category_id}, '{safe_name}')")

    print("Processing books and creating linking table entries...")
    for index, row in df.iterrows():
        book_id = index + 1

        safe_title = str(row['Title']).replace("'", "''") if pd.notna(row['Title']) else 'NULL'
        description = str(row['Description'])
        safe_description = description.replace("'", "''")
        description_sql = f"'{safe_description}'" if pd.notna(description) and description.strip() != '' else 'NULL'
        year = int(row['Year']) if pd.notna(row['Year']) else 'NULL'

        book_values.append(f"({book_id}, '{safe_title}', {description_sql}, {year})")

        for author_name in row['author_list']:
            if author_name in author_to_id:
                author_id = author_to_id[author_name]
                book_author_values.append(f"({book_id}, {author_id})")

        for category_name in row['category_list']:
            if category_name in category_to_id:
                category_id = category_to_id[category_name]
                book_category_values.append(f"({book_id}, {category_id})")

    print("Writing SQL files using batched multi-row inserts...")
    output_dir = "sql_inserts"
    os.makedirs(output_dir, exist_ok=True)

    with open(os.path.join(output_dir, 'authors.sql'), 'w', encoding='utf-8') as f:
        write_batched_sql(f, 'Authors', ['id', 'name'], author_values)

    with open(os.path.join(output_dir, 'categories.sql'), 'w', encoding='utf-8') as f:
        write_batched_sql(f, 'Categories', ['id', 'name'], category_values)

    with open(os.path.join(output_dir, 'books.sql'), 'w', encoding='utf-8') as f:
        write_batched_sql(f, 'Books', ['id', 'title', 'description', 'year'], book_values)

    with open(os.path.join(output_dir, 'book_authors.sql'), 'w', encoding='utf-8') as f:
        write_batched_sql(f, 'BookAuthors', ['bookId', 'authorId'], book_author_values)

    with open(os.path.join(output_dir, 'book_categories.sql'), 'w', encoding='utf-8') as f:
        write_batched_sql(f, 'BookCategories', ['bookId', 'categoryId'], book_category_values)

    print("Successfully generated all SQL files.")


if __name__ == "__main__":
    # To re-clean the source file from scratch, uncomment the next line.
    # clean_source_data(input_csv='BooksDatasetClean.csv', output_csv='cleaned_dataset.csv')


    cleaned_df = pd.read_csv('cleaned_dataset.csv')
    print(f"Loaded {len(cleaned_df)} books from cleaned_dataset.csv")

    if len(cleaned_df) > 500:
        cleaned_df = cleaned_df.sample(n=500, random_state=42).reset_index(drop=True)
        print(f"Reduced dataset to a random sample of {len(cleaned_df)} books.")

    generate_sql_files(cleaned_df)


