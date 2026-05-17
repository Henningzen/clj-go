(ns jansenh.clj-go.engine-test
  (:require [clojure.test :refer [deftest is testing]]
            [jansenh.clj-go.engine :as sut]
            [jansenh.clj-go.utilities :as utils]))

(deftest test-pos
  (let [board (utils/numeric-board 19)]
   (testing "We expect to find the corresponding [x y] value on the numeric board."
     (is (= 41  (sut/value-at-position (sut/position-> 2 2) board)))
     (is (= 42  (sut/value-at-position (sut/position-> 3 2) board)))
     (is (= 49  (sut/value-at-position (sut/position-> 10 2) board)))
     (is (= 193 (sut/value-at-position (sut/position-> 2 10) board)))
     (is (= 201 (sut/value-at-position (sut/position-> 10 10) board))))))

(deftest test-pos-2
  (let [board (utils/numeric-board 19)]
   (testing "We expect to find the corresponding [x y] value on the numeric board."
     (is (= 1 (sut/value-at-position (sut/position-> 0 0) board)))
     (is (= 19 (sut/value-at-position (sut/position-> 18 0) board)))
     (is (= 343 (sut/value-at-position (sut/position-> 0 18) board)))
     (is (= 361 (sut/value-at-position (sut/position-> 18 18) board))))))

(deftest test-pos-edges
  (let [board (utils/numeric-board 19)]
   (testing "We expect to find the corresponding [x y] value on the numeric board edges."
     (is (= 1   (sut/value-at-position (sut/position-> 0 0) board)))
     (is (= 19  (sut/value-at-position (sut/position-> 18 0) board)))
     (is (= 343 (sut/value-at-position (sut/position-> 0 18) board)))
     (is (= 361 (sut/value-at-position (sut/position-> 18 18) board))))))

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
  (let [board (utils/numeric-board 19)]
    (testing "We expect to find the neighbours values on the numeric board."
      (is (= [nil 2 20 nil]    (sut/neighbours-value (sut/position-> 0 0) board)))
      (is (= [nil nil 38 18]   (sut/neighbours-value (sut/position-> 18 0) board)))
      (is (= [342 nil nil 360] (sut/neighbours-value (sut/position-> 18 18) board)))
      (is (= [324 344 nil nil] (sut/neighbours-value (sut/position-> 0 18) board))))))

(deftest test-move
  (testing "We are creating move instructions via structured argument."
    (is (= :black (:player (utils/stone :black 1 2))))
    (is (= :white (:player (utils/stone :white 3 4))))
    (is (=  3 (:x-pos (utils/stone :black 3 4))))
    (is (=  4 (:y-pos (utils/stone :white 3 4 ))))))

(deftest test-update-position-at-board
  (testing "We expect to update position at board."
    (is (= :black (->> (utils/empty-board-vector 19)
                       (sut/update-position-at-board (utils/stone :black 0 0))
                       (sut/value-at-position (sut/position-> 0 0)))))
    (is (= :white (->> (utils/empty-board-vector 19)
                       (sut/update-position-at-board (utils/stone :white 19 19))
                       (sut/value-at-position (sut/position-> 19 19)))))))

(deftest test-go-string
  "Let's test go-strings!"
  (let [first-string {:player :black :stones #{[1 1]} :liberties #{[2 2] [2 3]}}
        second-string {:player :white :stones #{[5 5]} :liberties #{[2 2] [2 3]}}]
    (testing "Test basic go-string"
      (is (= 2 (count (:liberties first-string))))
      (is (= 1 (count (:stones first-string)))))))


(comment


  (count (utils/empty-board-vector 19))


  )
