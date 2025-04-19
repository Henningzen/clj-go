(ns no.jansenh.clj-go.core
  (:require [clojure.set :as set]))

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
        stones         (set (:stones m #{}))  ; Ensure set, defaults to empty set
        liberties      (:liberties m 0)       ; Defaults to zero (00
        conj-string    (first rst) ; Get the first map from the rest args
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
       :liberties conj-liberties}

      ;; 5. Else: Players are valid but different. Don't merge.
      :else
      m)))

(comment

  (def first-string {:player :black :stones #{[1 1]} :liberties 4})
  (def second-string {:player :white :stones #{[5 5]} :liberties 2})

  ;; Valid merge - OK
  (update-go-string first-string {:player :black :stones #{[1 2]} :liberties 3})

  ;; Players don't match - OK (returns original)
  (update-go-string first-string {:player :white :stones #{[2 2]} :liberties 1})

  ;; Initial player invalid - Throws ex-info
  (try
    (update-go-string {:player :red} {:player :black :stones #{[1 1]}})
    (catch clojure.lang.ExceptionInfo e
      (println "Caught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Incoming player invalid - Throws ex-info
  (try
    (update-go-string second-string {:player :non-valid
                                     :stones #{[0 0] [0 1]}
                                     :liberties 2})
    (catch clojure.lang.ExceptionInfo e
      (println "\nCaught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Nothing to merge - OK (returns original)
  (update-go-string first-string)

  ;; --->
)

nil
