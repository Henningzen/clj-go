(ns no.jansenh.clj-go.terminal-board-test
  (:require [no.jansenh.clj-go.terminal-board :as sut]
            [no.jansenh.clj-go.utilities :as utils]
            [clojure.test :refer [deftest is testing]]
            [clojure.pprint :as pp]))

(comment
  (def nboard utils/numeric-board)
  (def board utils/patterned-board)

  (sut/symbolic-board utils/patterned-board)
  (println (sut/symbolic-board utils/patterned-board))

  ;;--->
  )


