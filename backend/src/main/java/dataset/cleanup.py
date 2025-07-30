import pandas as pd

def clean_authors(df):
    df['Authors'] = df['Authors'].str.replace(r'^\s*By\s+', '', regex=True)
    return df

if __name__ == "__main__":
    # Load the dataset
    df = pd.read_csv('BooksDatasetClean.csv')

    # remove rows where 'Category' is null and where 'Publish Date (Year)' is null
    df = df.dropna(subset=['Category', 'Publish Date (Year)'])

    # Clean the author names
    df = clean_authors(df)

    # change some columns name
    columns_to_rename = {
    'Publish Date (Year)' : 'Year',
    'Publish Date (Month)' : 'Month',
    'Price Starting With ($)': 'Price',
    }
    df = df.rename(columns=columns_to_rename)

    df.to_csv('cleaned_dataset.csv', index=False)