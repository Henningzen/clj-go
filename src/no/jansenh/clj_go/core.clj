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
`   Multiple telescopic arity, positions are x-pos, y-pos, player and board."
  ([x-pos y-pos]
   {:x-pos    x-pos
    :y-pos    y-pos})

  ([x-pos y-pos board]
   {:x-pos    x-pos
    :y-pos    y-pos
    :board    board})

  ([x-pos y-pos player board]
   {:x-pos    x-pos
    :y-pos    y-pos
    :player   player
    :board    board}))

(defn position
  "Gets state of a given position on board.
   - position x is horisontal,
   - position y is vertical."
  [{:keys [x-pos y-pos board]}]
  (get-in board [y-pos x-pos]))

(defn neighbours-position
  "Gets the positions of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [{:keys [x-pos y-pos]}]
  [[x-pos (- y-pos 1)]
   [(+ x-pos 1) y-pos]
   [x-pos (+ y-pos 1)]
   [(- x-pos 1) y-pos]])

(defn neighbours-value
  "Gets the values of the neighbors on a given position, arranged
   as follows: north, east, south, and west."
  [{:keys [x-pos y-pos board]}]
  (let [neighbours (neighbours-position {:x-pos x-pos :y-pos y-pos :board board})]
    (into [] (map #(position {:x-pos (first %) :y-pos (second %) :board board}) neighbours))))

nil
