# Why Clojure? {.unnumbered}

This is the first question most people ask me when they learn that I work primarily with Clojure. The real answer is just that I really love it. It's the first programming language I've worked with that doesn't frustrate me to my core. It never feels like I'm fighting the language to be productive. Clojure is a minimalistic, simple language that gets out the way so you can just use it as a tool to get things done. The downside is that you end up spending most of your time thinking about actual problems -- the real issues that just take time and energy to figure out. Once you have the solution, though, implementing it in Clojure is a joy.

Anyway -- as much as I personally love the language, Clojure is not a common first choice for data science (yet!). It has everything you need to be productive working with data, and (I'd argue) it offers elegant solutions to most of the most common complaints with the current data science ecosystem.

Clojure is among the [most stable](https://dl.acm.org/doi/10.1145/3386321), [most loved](https://insights.stackoverflow.com/survey/2021#section-most-loved-dreaded-and-wanted-programming-scripting-and-markup-languages), and [most lucrative](https://insights.stackoverflow.com/survey/2021#section-salary-salary-and-experience-by-language) languages out there. It's free, open source, and has excellent performance thanks to being [hosted on the JVM](https://clojure.org/about/jvm_hosted). There are thousands of libraries available for Clojure, in addition to the endless Java libraries that are seamlessly available too.

If you prefer to listen rather than read, the rest of this section is mostly a summary of the points I made in [my talk at the 2023 Conj](https://www.youtube.com/watch?v=MguatDl5u2Q), which you could watch instead.

## Complete toolkit

It would be a bit of a stretch to say that Clojure is at feature parity with the current leading data science ecosystems, but it is close. Clojure's data science toolkit is complete and can support any kind of work you'd need to do with data. Some libraries are more mature than others, but every major aspect of working with data is covered. This table shows a comparison of Clojure's ecosystem to Python's and R's. Obviously there are many ways to accomplish any given task and these libraries are just a small sample of what's available, but the point is to illustrate that anything you need to do with data today, you (technically) can do in Clojure. It will not be the most well supported or standard way to do it, but it is possible, and in many cases Clojure offers improvements over other current options.

| Tool                     | python                   | R                        | Clojure                                                 |
|--------------------------|--------------------------|--------------------------|---------------------------------------------------------|
| Math, number crunching   | SciPy (Numpy)            | base R                   | **[dtype-next](https://github.com/cnuernber/dtype-next), [Fastmath](https://github.com/generateme/fastmath), [Neanderthal](https://github.com/uncomplicate/neanderthal)** |
| Table processing         | Pandas, Polars           | data.table, dplyr, tidyr | **[tech.ml.dataset](https://github.com/techascent/tech.ml.dataset), [tablecloth](https://github.com/scicloj/tablecloth)** |
| Stats                    | StatsModels              | base R                   | **[Fastmath](https://github.com/generateme/fastmath)** |
| Dataviz                  | matplotlib               | ggplot2                  | **[cljplot](https://github.com/generateme/cljplot), [hanami](https://github.com/jsa-aerial/hanami/), ongoing work implementing ggplot in Clojure** |
| Dashboards               | Streamlit                | Shiny                    | **[DataRabbit](https://www.datarabbit.com), [Clerk](https://clerk.vision)** |
| Machine learning         | Scikit-learn             | caret, mlr3, tidymodels  | **[scicloj.ml](https://github.com/scicloj/scicloj.ml)** |
| Deep learning            | Keras, Pytorch           | nnet, neuralnet, h2o     | **[Deep Diamond](https://github.com/uncomplicate/deep-diamond)** |
| Scaling, parallelization | Dask                     | ---                      | **[Clojask](https://github.com/clojure-finance/clojask)** |
| Spark                    | PySpark                  | SparkR, sparklyr         | **[Geni](https://github.com/zero-one-group/geni)** |
| Developer tooling        | Jupyter Notebook, VSCode | RStudio, KnitR           | **[Clay](https://github.com/scicloj/clay), [Clerk](https://clerk.vision), [Portal](https://github.com/djblue/portal), [Calva notebooks](https://calva.io/notebooks/)** |

## Interop

## Speed

## Acecssible

- Free
- Open source
- Welcoming
- Active

## Reproducibility

## Immutability

## Stability

## Functional

## Better "notebooks"

## Portability

## Ergonomics
