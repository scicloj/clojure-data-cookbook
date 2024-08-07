# Data Import {#sec-data-import}

To work with your data in Clojure, first you have to load the data into a Clojure namespace. This section covers how to load data from many different sources, from files to databases to web pages, and more.

This is also a good place to introduce [tablecloth](https://github.com/scicloj/tablecloth/). This book covers advanced tablecloth usage in later chapters, but for now all you need to know is that it is the dataset manipulation library upon which the Clojure data science ecosystem is built, and the recommended starting point for working with data. Many common sources of data, like files, URLs, and input streams, are supported by tablecloth out of the box so in some cases, loading data is as simple as calling the tablecloth dataset constructor. In others, it requires some extra dependencies or data manipulation to get the data into a reasonable tidy format suitable for further analysis.

Tablecloth is generally intelligent about inferring the correct datatypes of columns (thanks to the underlying [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) library upon which it is built), and making other reasonable assumptions about things like column headers. [Tablecloth's dataset constructor accepts many optional arguments](https://scicloj.github.io/tablecloth/index.html#dataset-api) to customize its behaviour if necessary.

This section of the book covers how to access data from the following sources:

- Tabular file formats, including CSV and a variety of proprietary spreadsheet formats, like Excel and Google Sheets.
- Columnar and compressed file formats, including Nippy, Zarr, Parquet, and Arrow.
- Databases, both relational and graph.
- The Internet, including scraping web pages and processing the often hierarchical and deeply-nested data returned by many APIs.

# TODO (?)
- Other misc file formats
  - SASS/SPSS
  - XML


<!-- ;; ## From a URL -->

<!-- ;; ## From a hand-written data entry -->

<!-- ;; ## From a database -->

<!-- ;; ## From a web page -->


<!-- ;; ## Other considerations -->

<!-- ;; ### File encoding -->
<!-- ;; ### Column header formatting -->
<!-- ;; ### Column data types -->


<!-- Text: CSV & JSON -->
<!-- Binary: Parquet, Delta Lake, AVRO & Excel -->
<!-- IPC: Feather, Arrow -->
<!-- Databases: MySQL, Postgres, SQL Server, Sqlite, Redshift & Oracle -->
<!-- Cloud storage: S3, Azure Blob & Azure File -->
