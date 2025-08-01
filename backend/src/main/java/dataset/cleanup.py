import pandas as pd

def clean_authors(df):
    df['Authors'] = df['Authors'].str.replace(r'^\s*By\s+', '', regex=True)
    return df

if __name__ == "__main__":
    # Load the dataset
    df = pd.read_csv('BooksDatasetClean.csv')

    df = df.drop(["Publisher", "Publish Date (Month)", "Price Starting With ($)"], axis=1)

    # remove rows where 'Category' is null and where 'Publish Date (Year)' is null
    df = df.dropna(subset=['Category', 'Publish Date (Year)'])

    # Clean the author names
    df = clean_authors(df)

    # change some columns name
    columns_to_rename = {
    'Publish Date (Year)' : 'Year',
    }
    df = df.rename(columns=columns_to_rename)

    df.to_csv('cleaned_dataset.csv', index=False)