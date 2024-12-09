# Notes

This is really just a dumping ground for project-related notes

---------------------

More details on [supported types here](https://github.com/techascent/tech.ml.dataset/blob/master/topics/supported-datatypes.md).


TODO: Explain when to use :double vs :type/numerical? What’s the difference?

JSON example: works as long as the data is an array of maps
```
(-> "https://vega.github.io/vega-lite/data/cars.json"
    tc/dataset)
```


Tribuo docs (libs required per model):

https://github.com/oracle/tribuo/blob/main/docs/PackageOverview.md


- For data wrangling section:
  - joining datsaets, with and without duplicated columns
