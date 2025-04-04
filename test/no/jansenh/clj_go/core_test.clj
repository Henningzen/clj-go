(ns no.jansenh.clj-go.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [no.jansenh.clj-go.core :as sut]))

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

;;; ----------------------------------------------------------------------------
;;;   Unit tests
;;;

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 29 (sut/position 2 2 numeric-board)))
    (is (= 30 (sut/position 3 2 numeric-board)))
    (is (= 37 (sut/position 10 2 numeric-board)))
    (is (= 141 (sut/position 10 10 numeric-board)))
    (is (= 133 (sut/position 2 10 numeric-board)))))

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board edges ."
    (is (= 1 (sut/position 0 0 numeric-board)))
    (is (= 13 (sut/position 12 0 numeric-board)))
    (is (= 169 (sut/position 12 12 numeric-board)))
    (is (= 157 (sut/position 0 12 numeric-board)))))

(deftest test-neighbours-position
  (testing "We expect to fint the neighbours positions on the numeric board."
    (is (= [[2 1] [3 2] [2 3] [1 2]] (sut/neighbours-position 2 2 numeric-board)))
    (is (= [[10 1] [11 2] [10 3] [9 2]] (sut/neighbours-position 10 2 numeric-board)))
    (is (= [[10 9] [11 10] [10 11] [9 10]] (sut/neighbours-position 10 10 numeric-board)))
    (is (= [[2 9] [3 10] [2 11] [1 10]] (sut/neighbours-position 2 10 numeric-board)))))

(deftest test-neighbours-value
  (testing "We expect to find the neighbours values on the numeric board."
    (is (= [nil 2 14 nil] (sut/neighbours-value 0 0 numeric-board)))
    (is (= [nil nil 26 12] (sut/neighbours-value 12 0 numeric-board)))
    (is (= [156 nil nil 168] (sut/neighbours-value 12 12 numeric-board)))
    (is (= [144 158 nil nil] (sut/neighbours-value 0 12 numeric-board)))))

