;;; src/jansenh/clj-go/utilities.clj  ---  Go game utilities

;   Copyright (c) Henning Jansen 2025 - 2026
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 2.0 (https://opensource.org/license/epl-2-0)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license. You must not remove this notice, or any other,
;   from this software.
;
;; Author:  Henning Jansen - henning.jansen@jansenh.no
;; Date:    September 2025
;; License: Eclipse Public License 2.0 - http://www.eclipse.org/legal/epl-2.0
;;-----------------------------------------------------------------------------

(ns jansenh.clj-go.utilities
  {:author "Henning Jansen"
    :doc    "Go game utilities."
    :added "0.1.1"})

(defn empty-board-vector
  "Default empty board vector, 'n by 'n in x-pos, y-pos vectors. Values are nil."
  [n]
  (vec (for [row (range n)]
         (vec (for [col (range n)]
                nil)))))

(defn stone
  "Helper function for stone representation maps.
   A stone has value, the player color,  and x y positions.

   Returns: a stone map representation '{:player :black :x-pos 0, :y-pos 0}'. "
  [player x-pos y-pos]
  {:player player
   :x-pos x-pos
   :y-pos y-pos})


(defn numeric-board
  "Data structure with sequence numbers.
   The data-structure is a representation of a board with n by n vector of
   vectors, numbers increasing from one.

   Returns:  board vector with a running number sequence 1 - {grid-size * grid-size}. "
  [grid-size]
  (let [grid-size grid-size]
    (vec (for [row (range grid-size)]
           (vec (for [col (range grid-size)]
                  (inc (+ col (* row grid-size)))))))))


(defn patterned-board
  "Scattered board with cycled values.
   Evenly distributed white, black stones with empty (nil) intersects.
   The data-structure is a representation of a board with
   19 by 19 vector with values :black :white and :nil.
   
   Returns: board vector with evenly toggled :white and :black. "
   [grid-size]
  (let [grid-size grid-size]
    
    (->> (take (* grid-size grid-size) (cycle [:black :nil :white :nil]))
         (partition grid-size)
         (mapv vec))))


