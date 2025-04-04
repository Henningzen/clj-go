(ns no.jansenh.clj-go.utilities
  (:require  [clojure.test :refer [deftest is testing]]))

;;; ----------------------------------------------------------------------------
;;;   Test data
;;;

;; We got a data-structure with numbers only, used for 
;; testing vectors and positions. The data-structure is
;; a representation of a board with 13 by 13 vector,
;; numbers 1 to 169.
(def numeric-board
  (vec (for [row (range 13)]
         (vec (for [col (range 13)]
                (inc (+ col (* row 13))))))))

;; We got a scattered board with cycled values, evenly
;; distributed white, black stones with empty intersects.
;; The data-structure is a representation of a board with
;; 13 by 13 vector with values :black :white and :nil.
(def patterned-board
  (->> (take 169 (cycle [:black :nil :white :nil]))
       (partition 13)
       (mapv vec)))

