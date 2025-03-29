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

(defn position
  "Gets state of a given position on board.
   - position x is horisontal,
   - position y is vertical."
  [x y b]
  (get-in b [y x]))

(defn neighbours-position
  "Gets the positions of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [x y b]
  [[x (- y 1)][(+ x 1) y][x (+ y 1)][(- x 1) y]])

(defn neighbours-value
  "Gets the values of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [x y b]
  (let [neighbours (neighbours-position x y b)]
    (into [] (map #(position (first %) (second %) b) neighbours))))

