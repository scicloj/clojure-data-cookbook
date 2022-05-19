# Draft Clojure cookbook outline
## 1. Basics
- How much is worth including as background?
- How much should we assume readers know about Clojure?
    - Write for an audience who know basic Clojure syntax, only reference language guides
- Already lots of good resources. E.g. [official Clojure guide on language basics](https://clojure.org/guides/learn/clojure%20)
- How to use this cookbook? Who is it for, what is the best way to use it?
- Possible topics to include:
    - Setting up a Clojure environment
    - Installing and using Clojure and its libraries
    - Executing Clojure code
    - Tour of the tool we're using (clay? clerk?)
    - Working with data in Clojure
        - Accessing subsets of larger data structures
        - Types of collections in Clojure, hashed (maps, sets) vs. sequential collections (lists, vectors)
        - Tour of useful collections operations? `filter` , `take` , `drop` , `nth` , `subvec`
        - Maybe a good place to introduce tablecloth? Way nicer tool for common dataset manipulation tasks
        - Comparisons to other languages?
            - E.g. how does negative indexing work? Some languages index backward from the end (python), some drop the element at that index (R)

## 2. Data input and output
### 2.1 How to get data into the notebook
- EDN data format
- Reading from a CSV file
    - Normalize values into consistent formats and types
    - Transforming headers
    - Specifying separators if it's not a `,`
    - Specify file encoding
- Reading from a URL, JSON
- Excel, other file formats? spss?
- Reading from a database
    - Connecting to a database
    - SQL results, SPARQL results
- Generating random sequences
- Generating repeatable sequences of dummy data

### 2.2 Dealing with messy data
- How do you know it's messy?
    - Multiple types mixed in one column
    - Multiple formats for a thing that's supposed to have one (e.g. phone numbers, postal codes)
    - Missing values
    - Arbitrary values meant to indicate missing (e.g. "NONE", "N/A", false, etc.)
- Parsing dates, formatting dates
- Parsing numbers, booleans
- Handling missing/blank cells

### 2.3 How to get data out of a notebook
- Writing to a CSV file
- Leave data in Clojure files
- Notebook artifacts
    - HTML pages
    - Other formats, options for exporting notebooks? PDFs?
    - Partial artifacts, e.g. export just a graph
- Writing to a database?

## 3. Data manipulation
- Sorting columns, rows, tables
    - Defaults and custom sorting functions
- Selecting one column or multiple columns
- Randomizing order
    - Repeatable randomisation
- De-duplicating values
- Finding unique rows
- Adding computed columns to data
    - e.g. converting a column with numbers to a category (>5 "yes", <5 "no"), summing multiple columns into a new one
- Removing columns
- Transforming values
    - Working with nested data structures, really nice libraries in Clojure for doing this ([specter](https://github.com/redplanetlabs/specter), [meander](https://github.com/noprompt/meander))
    - All values in a column
    - Conditional transformation (e.g. "truncate only 11 digit phone numbers to 10 digits")
- Rearranging order of columns
- Renaming columns
- Filtering rows
    - Single filter, multiple filters
- Aggregating rows (counts, groups)
- Concatenating datasets
- Merging datasets
    - When column headers are the same or different, on multiple columns
- Converting between wide and long formats?
- Summarizing data (mean, standard deviation, confidence intervals etc.)
- Working with sequential data
    - Smoothing out data
        - Calculating a moving average
        - Averaging a sequence in blocks
    - Run length encoding?
    - Filling `nil` s with last non-`nil` value?

## 4. Data visualisation (?)
### 4.1 Visualisation libraries
- Vega and vega-lite viewers in clerk/clay
- Introduce Hanami? *How is the community feeling about Hanami? Do people like it as a go-to tool for data viz or is there something else?*
- Getting data into the right format for these libraries (EDN maps)

### 4.2 Graphs
- Bar graphs
    - Of values
    - Of counts
- Line graphs
- Scatterplots
    - Handling overplotting (overlapping points)
- Distributions
    - Histograms and density plots
        - With multiple groups
    - Box plots
        - With mean
- Q-Q Plot
- Correlation matrix
- Network graph
- Heat map
- 3D Scatterplot
- Tree plot (dendrogram)
- Vector field
- Mosaic Chart
- Pie chart
- Maps
    - Geo data formats (GeoJSON, shapefiles)
    - Choropleths
        - Careful splitting up data for choropleths, see jenks algorithm — you cannot map totals using a choropleth thematic mapping technique (Kenneth Field)
- Layering graphs
    - Add regression lines
    - Adding a horizontal line
    - Adding a vertical line
    - Adding lines for the mean
    - Means and error bars
        - On bar graphs and line graphs
        - Over grouped bars
        - Over individual grouped bars
        - Error bars for within-subjects variables
    - Error bands
- Facets and matrices
    - Independent scales per facet
    - Organizing facets (vertical/horizontal stacks)
    - Labelling facets
    - Lines on facets, different line on different facets (?)

### 4.2 Meta-graph concerns
- Graph size
- Gridlines
- Colors
    - *Keep colour blindness in mind, colour is last choice for conveying information*
    - Mapping variables to colours
- Shapes and line types
- Adding titles to graphs
- Labelling axes
- Fonts
- Changing direction of axes
- Changing order of items on a discrete axis
- Transforming axes (e.g. log/linear)
- Labelling data points
- Legends
    - Changing labels, order of items
- Organizing multiple graphs
- Saving graphs
    - Image formats, SVG, PDF
    - [Vega embed](https://github.com/vega/vega-embed%20)


## 5. Statistics (?)
*Not sure how much of this it makes sense to include — need to balance teaching what these even mean with showing how to do them in Clojure, assuming someone already knows why they would use it and how to interpret the results. Maybe better as a whole separate project? Clojure doesn't have as many sophisticated libraries that just do all these with a single command.*
- Linear regression and correlation
- Logistic regression
- p-values
- t-test, z-test, ANOVA
- Confidence intervals
- Resampling
    - Bootstrapping, Jacknifing
- Frequency tests
    - Chi-square, Fisher’s Exact, Exact binomial, McNemar’s test
- Homogeneity of variance
    - Levene’s, Bartlett’s, Fligner-Killeen test
- Inter-rater reliability
    - Cohen’s Kappa, weighted Kappa, Scott's pi, Fleiss’s Kappa, Conger’s Kappa, intraclass correlation coefficient

## To be placed..
### Numbers
- Types of numbers, when to use which ones
    - Integers (`java.lang.Long` , `clojure.lang.BigInt` )
    - Decimals (`java.lang.Double` , `java.math.BigDecimal` )
- How to indicate different types of numbers, e.g. `1` vs `1.0` vs. `1.0M`
- Converting between types
- Gotchas to watch out for, e.g. trying to show a number that has infinitely repeating decimal places ⟶ `(/ 1.0M 6.0M)` wil throw an error ([great guide](https://blog.brunobonacci.com/2016/05/11/clojure-basics/%20))
- Rounding numbers
- Comparing numbers in Clojure, i.e. `(= 1.0M 1.0 1) => true` , not the case in every language
    - Comparisons with `nil` , booleans

### Strings
- Not sure how much is useful to include.. maybe just quick mention of regex support/syntax and `clojure.string` standard lib?
- String interpolation, using variables in strings

### Timestamps
- How to work with Clojure/Java time formats
    - Adding and subtracting times/dates
    - Converting between date and time formats
    - Comparing dates (relatively, i.e. checking for earlier/later than a certain date/time)

### Interop
- Working with python libraries in Clojure (see [libpython-clj](https://github.com/clj-python/libpython-clj))
    - How to include python libraries with libpython-clj
    - Using python classes as Clojure namespaces
    - Examples of some of the most common ones that don't have equivalents in Clojure? (ML/NLP libraries?)
        - Mention some common ones that _do_ have equivalents (data manipulation, visualization?)

### Performance
- Benchmarking blocks of code
- Tips for avoiding performance bottlenecks
    - Careful with realizing large lazy sequences
    - Careful holding on to a reference to the head of a sequence

## Resources
### Talks
- [Data Science Walkthrough by Daniel](https://www.youtube.com/watch?v=28os_84XA5w )
- [Wrangling data with Tablecloth by Mei Beisaron](https://www.youtube.com/watch?v=VD17eB6vVto )
- [Intro to Statistical Inference by Rohit Thadani](https://www.youtube.com/watch?v=X4FisyEg1zo )

### Papers
- [Confidence Intervals from Normalized Data:
A correction to Cousineau (2005)](https://tqmp.org/RegularArticles/vol04-2/p061/p061.pdf%20)
- [Confidence intervals in within-subject designs:
A simpler solution to Loftus and Masson’s method](https://pdfs.semanticscholar.org/e585/7a9a0c09a92624a466a063e03104c1c3a605.pdf%20)
- [Using confidence intervals in within-subject designs](https://link.springer.com/content/pdf/10.3758/BF03210951.pdf%20)


### Blog posts
**Choropleths**
- [Splitting up data for choropleths](https://macwright.com/2013/02/18/literate-jenks.html%20)
- [Problems with choropleths](https://www.esri.com/arcgis-blog/products/product/mapping/mapping-coronavirus-responsibly/%20)

**Python interop**
- [Using mathplotlib and numpy](https://nextjournal.com/kommen/parens-for-polyglot)
- [Interop with NLP libraries](http://gigasquidsoftware.com/blog/2020/01/24/clojure-interop-with-python-nlp-libraries/)

