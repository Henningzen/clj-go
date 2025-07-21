;; -----------------------------------------------------------------------------
;; File: src/no/jansenh/clj_go/core.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 2025
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------

(ns no.jansenh.clj-go.core
  (:require [clojure.set :as set]
            [no.jansenh.clj-go.terminal-board :as tb]))

;; -----------------------------------------------------------------------------
;; clj-go.core
;; -----------
;; Core namespace.
;;
;; authors:   Henning Jansen            henning.jansen@jansenh.no
;; since:     0.1.0-SNAPSHOT            2025-01-29
;; version:   0.1.1-SNAPSHOT            2025-06-04
;; -----------------------------------------------------------------------------


(def empty-board
  "Default empty board, 19 by 19 in x-pos, y-pos vectors. Values are nil."
  (vec (for [row (range 19)]
         (vec (for [col (range 19)]
                nil)))))

(defn stone
  "A stone has a position and a value, the color."
  [x-pos y-pos player]
  {:player player
   :x-pos x-pos
   :y-pos y-pos})


(defn position->
  "Helper function for transforming position vector or map.
   1. Create a vector [x y] from a map with {:x-pos x, :y-pos y}.
   2. Creates a {:x-pos x, :y-pos y} map from [x y] values."
  ([m]
   [(:x-pos m) {:y-pos m}])
  ([x y]
   {:x-pos x
    :y-pos y}))


(defn update-position-at-board
  "Updates the board at the specified x and y position with the given value.
   The value is a player :black or :white or nil.

   Returns a new board with the updated position."
  [{:keys [x-pos y-pos player]} board]
  (assoc-in board [y-pos x-pos] player))


(defn value-at-position
  "Gets state of a given position on board.
   - position x is horisontal,
   - position y is vertical."
  [{:keys [x-pos y-pos]} board]
  (get-in board [y-pos x-pos]))


(defn neighbours-at-position
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
  [{:keys [x-pos y-pos]} board]
  (let [neighbours (neighbours-at-position {:x-pos x-pos 
                                         :y-pos y-pos})]
    (into [] 
          (map #(value-at-position {:x-pos (first %) :y-pos (second %)} board)
               neighbours))))


(defn go-string
  "Update or merge a go-string. Merges stones if players match and are valid.

   The first arity return an empty go-string.

   The second arity in the function takes one Go string map and one additional
   Go string map.

   The function is agnostic to if we will update an existing Go string with
   a new version of itself, or if we are merging another Go string in top of
   the existing Go string. 

   Returns original map (m) if players don't match or if no map is
   provided for merging.

   Throws ex-info if the initial player or the player in the
   map is invalid.

   NOTE: The function supports merging only one string (for now).

   NOTE: Operations on 'let with (set...) and (conj- ...) use default values
         as fallback for consistency."
  ([]
   {:player nil :stones #{} :liberties #{}})
  ([m & rst]
   (let [player         (:player m)
         stones         (set (:stones m #{}))
         liberties      (set (:liberties m #{}))
         conj-string    (first rst)
         conj-player    (when conj-string (:player conj-string))
         conj-stones    (when conj-string (set (:stones conj-string #{})))
         conj-liberties (when conj-string (:liberties conj-string liberties))]

     (cond
       ;; 1. Validate player.
       (not (#{:black :white} player))
       (throw (ex-info "Invalid player value in initial map."
                       {:type ::invalid-player
                        :reason :initial-map-invalid
                        :player player
                        :map m}))

       ;; 2. Nothing to merge? We are done and return original input map,
       ;;    which can be a entirely new go-string, with the let' data from
       ;;    the input parameters added..
       (not conj-string)
       m

       ;; 3.Validate player in merged map.
       (not (#{:black :white} conj-player))
       (throw (ex-info "Invalid player value in update map."
                       {:type ::invalid-player
                        :reason :update-map-invalid
                        :player conj-player
                        :update-map conj-string
                        :initial-map m}))

       ;; 4. Does the validated players match? Merge
       (= player conj-player)
       {:player player
        :stones (set/union stones conj-stones)
        :liberties (set/union liberties conj-liberties)}

       ;; 5. Else: Players are valid but different. Don't merge.
       :else m))))


(defn remove-liberty
  "Remove libery from go-string..
   The liberty has format [x-pos y-pos]."
  ([go-string lib]
   (update go-string :liberties disj lib)))


(defn add-liberty
  "Add libery to go-string.
   The liberty has format [x-pos y-pos]."
  ([go-string lib]
   (update go-string :liberties conj lib))
  #_([board & libs]                      ;; TODO Adapt to collection of lib's
     (update board :liberties into libs)))


(defn go-string->board
  "Helper function that will apply a go-string on a board.
   NOTE: the intentional design:
         - We take the board from args and use as the reduce target.
         - The stones are a set of tuples, and we deconstruct them in
           the let binding as x and y.

   Arguments:
     - gs is a go-string
     - b is a board vector (empty-board is the reference format).
  "
  [gs b]
  (let [player (:player gs)
        stones (:stones gs)]
    (reduce (fn [board [x y]]
              (update-position-at-board (stone x y player) board))
            b
            stones)))
