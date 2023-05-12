(ns chapter-1-intro.1-1-welcome
  (:require
   [nextjournal.clerk :as clerk]))

(clerk/md (slurp "book/chapter_1_intro/1_1_welcome.md"))
