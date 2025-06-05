(ns no.jansenh.clj-go.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [no.jansenh.clj-go.core :as sut]
            [no.jansenh.clj-go.utilities :as utils]))

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 29  (sut/value-at-position (sut/position-> 2 2)   utils/numeric-board)))
    (is (= 30  (sut/value-at-position (sut/position-> 3 2)   utils/numeric-board)))
    (is (= 37  (sut/value-at-position (sut/position-> 10 2)  utils/numeric-board)))
    (is (= 141 (sut/value-at-position (sut/position-> 10 10) utils/numeric-board)))
    (is (= 133 (sut/value-at-position (sut/position-> 2 10)  utils/numeric-board)))))

(deftest test-pos-edges
  (testing "We expect to find the corresponding [x y] value on the numeric board edges ."
    (is (= 1   (sut/value-at-position (sut/position-> 0 0)   utils/numeric-board)))
    (is (= 13  (sut/value-at-position (sut/position-> 12 0)  utils/numeric-board)))
    (is (= 169 (sut/value-at-position (sut/position-> 12 12) utils/numeric-board)))
    (is (= 157 (sut/value-at-position (sut/position-> 0 12)  utils/numeric-board)))))

(deftest test-neighbours-at-position
  (testing "We expect to fint the neighbours positions on the numeric board."
    (is (= [[2 1] [3 2] [2 3] [1 2]]
           (sut/neighbours-at-position (sut/position-> 2 2))))
    (is (= [[10 1] [11 2] [10 3] [9 2]]
           (sut/neighbours-at-position (sut/position-> 10 2))))
    (is (= [[10 9] [11 10] [10 11] [9 10]]
           (sut/neighbours-at-position (sut/position-> 10 10))))
    (is (= [[2 9] [3 10] [2 11] [1 10]]
           (sut/neighbours-at-position (sut/position-> 2 10))))))

(deftest test-neighbours-value
  (testing "We expect to find the neighbours values on the numeric board."
    (is (= [nil 2 14 nil]    (sut/neighbours-value (sut/position-> 0 0)   utils/numeric-board)))
    (is (= [nil nil 26 12]   (sut/neighbours-value (sut/position-> 12 0)  utils/numeric-board)))
    (is (= [156 nil nil 168] (sut/neighbours-value (sut/position-> 12 12) utils/numeric-board)))
    (is (= [144 158 nil nil] (sut/neighbours-value (sut/position-> 0 12)  utils/numeric-board)))))

(deftest test-move
  (testing "We are creating move instructions via structured argument"
    (is (= :black (:player (sut/stone 1 2 :black))))
    (is (= :white (:player (sut/stone 3 4 :white))))
    (is (=  3 (:x-pos (sut/stone 3 4 :black))))
    (is (=  4 (:y-pos (sut/stone 3 4 :white))))))

nil
