(ns no.jansenh.clj-go.core)

;;;;
;;;;  clj-go.core
;;;;  -----------
;;;;  Core namespace.
;;;;
;;;;  Henning Jansen 2025  Copyright © henning.jansen@jansenh.no
;;;;  Distributed under the GNU General Public License v3.0 as
;;;;  described in the root of this project.
;;;;

(defn move
  "Takes a vector of positional arguments and return map with structured argument.
   Multiple telescopic arity, positions are x, y, player and board."
  ([x-pos y-pos]
   (let [x-pos  x-pos
         y-pos  y-pos]
     {:x-pos    x-pos
      :y-pos    y-pos}))

  ([x-pos y-pos board]
   (let [x-pos  x-pos
         y-pos  y-pos
         board  board]
     {:x-pos    x-pos
      :y-pos    y-pos
      :board    board}))

  ([x-pos y-pos player board]
   (let [x-pos  x-pos
         y-pos  y-pos
         player player
         board  board]
     {:x-pos    x-pos
      :y-pos    y-pos
      :player   player
      :board    board})))

(defn position
  "Gets state of a given position on board.
   - position x is horisontal,
   - position y is vertical."
  [m]
  (let [x (:x-pos m)
        y (:y-pos m)
        b (:board m)]
    (get-in b [y x])))

(defn neighbours-position
  "Gets the positions of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [m]
  (let [x (:x-pos m)
        y (:y-pos m)]
    [[x (- y 1)][(+ x 1) y][x (+ y 1)][(- x 1) y]]))

(defn neighbours-value
  "Gets the values of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [m]
  (let [x (:x-pos m)
        y (:y-pos m)
        b (:board m)]
    (let [neighbours (neighbours-position (move x y b))]
     (into [] (map #(position {:x-pos (first %) :y-pos (second %) :board b}) neighbours)))))

