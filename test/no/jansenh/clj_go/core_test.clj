(ns no.jansenh.clj-go.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [no.jansenh.clj-go.core :as sut]
            [no.jansenh.clj-go.utilities :as utils]))

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 41  (sut/value-at-position (sut/position-> 2 2) utils/numeric-board)))
    (is (= 42  (sut/value-at-position (sut/position-> 3 2) utils/numeric-board)))
    (is (= 49  (sut/value-at-position (sut/position-> 10 2) utils/numeric-board)))
    (is (= 201 (sut/value-at-position (sut/position-> 10 10) utils/numeric-board)))
    (is (= 193 (sut/value-at-position (sut/position-> 2 10) utils/numeric-board)))))

(deftest test-pos-2
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 1 (sut/value-at-position (sut/position-> 0 0) utils/numeric-board)))
    (is (= 343 (sut/value-at-position (sut/position-> 0 18) utils/numeric-board)))
    (is (= 19 (sut/value-at-position (sut/position-> 18 0) utils/numeric-board)))
    (is (= 361 (sut/value-at-position (sut/position-> 18 18) utils/numeric-board)))
    ))

(deftest test-pos-edges
  (testing "We expect to find the corresponding [x y] value on the numeric board edges."
    (is (= 1   (sut/value-at-position (sut/position-> 0 0) utils/numeric-board)))
    (is (= 19  (sut/value-at-position (sut/position-> 18 0) utils/numeric-board)))
    (is (= 361 (sut/value-at-position (sut/position-> 18 18) utils/numeric-board)))
    (is (= 343 (sut/value-at-position (sut/position-> 0 18) utils/numeric-board)))))

(deftest test-position->
 (testing "We expect to convert positions map and vector."
    (is (= {:x-pos 1 :y-pos 1} (sut/position-> 1 1)))
    (is (= [1 1] (sut/position-> {:x-pos 1 :y-pos 1})))))

(deftest test-neighbours-at-position
  (testing "We expect to find the neighbours positions on the numeric board."
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
    (is (= [nil 2 20 nil]    (sut/neighbours-value (sut/position-> 0 0) utils/numeric-board)))
    (is (= [nil nil 38 18]   (sut/neighbours-value (sut/position-> 18 0) utils/numeric-board)))
    (is (= [342 nil nil 360] (sut/neighbours-value (sut/position-> 18 18) utils/numeric-board)))
    (is (= [324 344 nil nil] (sut/neighbours-value (sut/position-> 0 18) utils/numeric-board)))))

(deftest test-move
  (testing "We are creating move instructions via structured argument."
    (is (= :black (:player (sut/stone 1 2 :black))))
    (is (= :white (:player (sut/stone 3 4 :white))))
    (is (=  3 (:x-pos (sut/stone 3 4 :black))))
    (is (=  4 (:y-pos (sut/stone 3 4 :white))))))

(deftest test-update-position-at-board
  (testing "We expect to update position at board."
    (is (= :black (->> sut/empty-board
                       (sut/update-position-at-board (sut/stone 0 0 :black))
                       (sut/value-at-position (sut/position-> 0 0)))))
    (is (= :white (->> sut/empty-board
                       (sut/update-position-at-board (sut/stone 19 19 :white))
                       (sut/value-at-position (sut/position-> 19 19)))))))

(deftest test-go-string
  "Let's test go-strings!"
  (let [first-string {:player :black :stones #{[1 1]} :liberties #{[2 2] [2 3]}}
        second-string {:player :white :stones #{[5 5]} :liberties #{[2 2] [2 3]}}]
    (testing "Test basic go-string"
      (is (= 2 (count (:liberties first-string))))
      (is (= 1 (count (:stones first-string)))))))
