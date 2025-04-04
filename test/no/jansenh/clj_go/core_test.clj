(ns no.jansenh.clj-go.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [no.jansenh.clj-go.core :as sut]
            [no.jansenh.clj-go.utilities :as utils]))

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 29 (sut/position  {:x-pos 2 :y-pos 2 :board utils/numeric-board})))
    (is (= 30 (sut/position  {:x-pos 3 :y-pos 2 :board utils/numeric-board})))
    (is (= 37 (sut/position  {:x-pos 10 :y-pos 2 :board utils/numeric-board})))
    (is (= 141 (sut/position {:x-pos 10 :y-pos 10 :board utils/numeric-board})))
    (is (= 133 (sut/position {:x-pos 2 :y-pos 10 :board utils/numeric-board})))))

(deftest test-pos-edges
  (testing "We expect to find the corresponding [x y] value on the numeric board edges ."
    (is (= 1 (sut/position (sut/move 0 0 utils/numeric-board))))
    (is (= 13 (sut/position (sut/move 12 0 utils/numeric-board))))
    (is (= 169 (sut/position (sut/move 12 12 utils/numeric-board))))
    (is (= 157 (sut/position (sut/move 0 12 utils/numeric-board))))))

(deftest test-neighbours-position
  (testing "We expect to fint the neighbours positions on the numeric board."
    (is (= [[2 1] [3 2] [2 3] [1 2]] (sut/neighbours-position (sut/move 2 2 utils/numeric-board))))
    (is (= [[10 1] [11 2] [10 3] [9 2]] (sut/neighbours-position (sut/move 10 2 utils/numeric-board))))
    (is (= [[10 9] [11 10] [10 11] [9 10]] (sut/neighbours-position (sut/move 10 10 utils/numeric-board))))
    (is (= [[2 9] [3 10] [2 11] [1 10]] (sut/neighbours-position (sut/move 2 10 utils/numeric-board))))))

(deftest test-neighbours-value
  (testing "We expect to find the neighbours values on the numeric board." 
    (is (= [nil 2 14 nil] (sut/neighbours-value (sut/move 0 0 utils/numeric-board))))
    (is (= [nil nil 26 12] (sut/neighbours-value (sut/move 12 0 utils/numeric-board))))
    (is (= [156 nil nil 168] (sut/neighbours-value (sut/move 12 12 utils/numeric-board))))
    (is (= [144 158 nil nil] (sut/neighbours-value (sut/move 0 12 utils/numeric-board))))))

(deftest test-move
  (testing "We are creating move instructions via structured argument"
    (is (= :black (:player (sut/move 1 2 :black utils/numeric-board))))
    (is (= :white (:player (sut/move 3 4 :white utils/numeric-board))))
    (is (=  3 (:x-pos (sut/move 3 4 utils/numeric-board))))
    (is (=  4 (:y-pos (sut/move 3 4 utils/numeric-board))))))
