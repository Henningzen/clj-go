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
;; version:   0.1.1-SNAPSHOT            2025-04-04
;; -----------------------------------------------------------------------------

(defn move
  "Takes a vector of positional arguments and return map with structured argument.
   Multiple telescopic arity, positions are x-pos, y-pos, player and board."
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

(def empty-board
  "Default empty board, 13 by 13 in x-pos, y-pos vectors. Values are nil."
  (vec (for [row (range 13)]
         (vec (for [col (range 13)]
                nil)))))

(defn update-board
  "Updates the board at the specified x and y position with the given value.
   Returns a new board with the updated position."
  [board x y value]
  (assoc-in board [y x] value))

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

(defn stone
  [player x-pos y-pos]
  {:player player
   :x-pos x-pos
   :y-pos y-pos})

(defn go-string
  "The initial shape of a go-string.
   Handles default values for missing keys in the input map.
   :stones will be a set of [x y] coordinate vectors.
   :liberties will be an integer.
   Returns an empty map if :player is not :black or :white."
  ([]
   {:player nil
    :stones #{} ;
    :liberties #{}})
  ([m]
   (let [player    (:player m)
         stones    (set (:stones m #{}))     ;; Defaults to empty set if none.
         liberties (set (:liberties m #{}))] ;; Defaults to empty set if none.
     (if (#{:black :white} player)
       {:player player
        :stones stones
        :liberties liberties}
       {})
     )))

(defn update-go-string
  "Update or merge a go-string. Merges stones if players match and are valid.

   The function is agnostic to if we will update an existing Go string with
   a new version of itself, or if we are merging another Go string in top of
   the existing Go string. The function takes one Go string map and one
   additional Go string map.

   Returns original map (m) if players don't match or if no map is
   provided for merging.
   Throws ex-info if the initial player or the player in the update
   map is invalid.
   The function supports mering only one string (for now)."

  [m & rst]
  (let [player         (:player m)
        stones         (set (:stones m #{}))    ; Ensure set, defaults to empty set.
        liberties      (set (:liberties m #{}))       ; Defaults to empty set.
        conj-string    (first rst)              ; Get the first map from the rest args.
        conj-player    (when conj-string (:player conj-string))
        conj-stones    (when conj-string (set (:stones conj-string #{})))
        conj-liberties (when conj-string (:liberties conj-string liberties))] ; Default to m's liberties

    (cond
      ;; 1. Validate player.
      (not (#{:black :white} player))
      (throw (ex-info "Invalid player value in initial map."
                      {:type ::invalid-player
                       :reason :initial-map-invalid
                       :player player
                       :map m}))

      ;; 2. Nothing to merge? Return original map.
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
      :else
      m)))


(defn remove-liberty
  "Function will take and return 'board -> board’ with one liberty removed.
   The liberty has format [x-pos y-pos]."
  ([board lib]
   (update board :liberties disj lib)))

(defn add-liberty
  "Function will take and return 'board -> board’ with one liberty added.
   The liberty has format [x-pos y-pos]."
  ([board lib]
   (update board :liberties conj lib))
  #_([board & libs]                      ;; TODO Adapt to collection of lib's
     (update board :liberties into libs)))
