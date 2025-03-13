(ns no.jansenh.clj-go.board
  (:require [seesaw.color :as color]
            [seesaw.core :as s]
            [seesaw.graphics :as g]))
;;;;
;;;;  Clj-go board
;;;;  ------------
;;;;
;;;;  Clojure Go Game board implementation, using Seesaw clojure wrapper
;;;;  for Java Swing AWT graphics.
;;;;
;;;;  Henning Jansen 2025  Copyright © henning.jansen@jansenh.no
;;;;  Distributed under the GNU General Public License v3.0 as described in the
;;;;  root of this project.
;;;;
;;;;  The Board has a basic state with:
;;;;    - black and white stones on a grid,
;;;;    - current player :black or :white
;;;;    - and events capturing mouse interaction from placing stones on the
;;;;      board and clicking buttons.
;;;;
;;;;  We got button for:
;;;;    1. pass turn
;;;;    2. resign
;;;;    3. close
;;;;
;;;; The board has an API for interacting with the game programatically
;;;; currently supporting player :white. The API support
;;;;   - :white add stone to the board, return true/false.
;;;;   - :white pass move.
;;;;
;;;;     Enjoy!

;;;  Definitions
;;;
(def board-size 12)          ; cells define grid intersections (+ 1 board-size)
(def cell-size 50)
(def stone-radius 17)
(def board-margin 40)
(def total-size (+ (* board-size cell-size) (* 2 board-margin)))


;;;  Reign in blood!
;;;
;;;     This is where evil reside.
;;;
(def game-state (atom {:board (vec (repeat (inc board-size)
                                           (vec (repeat (inc board-size) nil))))
                       :current-player :black}))


;;; ----------------------------------------------------------------------------
;;;    Utility functions
;;;

(defn get-intersection-position
  "Convert board coordinates to pixel coordinates"
  [i j]
  [(+ board-margin (* i cell-size))
   (+ board-margin (* j cell-size))])

(defn get-board-position
  "Convert pixel coordinates to the nearest board intersection"
  [x y]
  (let [i (Math/round (float (/ (- x board-margin) cell-size)))
        j (Math/round (float (/ (- y board-margin) cell-size)))]
    (when (and (<= 0 i board-size) (<= 0 j board-size))
      [i j])))

(defn place-stone
  "Place a stone on the board if the position is valid"
  [i j player]
  (when (and (<= 0 i board-size)
             (<= 0 j board-size)
             (nil? (get-in @game-state [:board j i])))
    (swap! game-state
           (fn [state]
             (-> state
                 (assoc-in [:board j i] player)
                 (assoc :current-player (if (= player :black) :white :black)))))))

(defn pass-turn
  "Pass the current player's turn"
  []
  (swap! game-state update :current-player
         (fn [current-player]
           (if (= current-player :black) :white :black))))

(defn count-stones
  "Count the number of black and white stones on the board"
  []
  (let [board (:board @game-state)
        stones (for [i (range (inc board-size))
                     j (range (inc board-size))
                     :let [stone (get-in board [j i])]
                     :when stone]
                 stone)
        black-count (count (filter #(= % :black) stones))
        white-count (count (filter #(= % :white) stones))]
    {:black black-count :white white-count}))

(defn handle-resign
  "Dummy function for handling resignation"
  []
  (let [stone-counts (count-stones)]
    (println "Game resigned!")
    (println (str "Black stones: " (:black stone-counts)))
    (println (str "White stones: " (:white stone-counts)))
    stone-counts))


(defn repaint!
  "Find and force repaint on all Go game frames."
  []
  (javax.swing.SwingUtilities/invokeLater
   (fn []
     (doseq [frame (s/all-frames)
             :when (= (.getTitle frame) "Game of Go")]
       ;; Force complete repaint of the entire frame
       (.validate frame)
       (.repaint frame)))))

;;; ----------------------------------------------------------------------------
;;;     ML Player - automata using machine learning.
;;;
;;;        The game is being set up with player white being non-human, interacting
;;;        with game-state via an api.
;;;

(defn make-white-move
  "External function for placing a white stone on the board.
   Function has side-effect with a repaint! on the JavaFX external libraries.
   Takes board coordinates [i j] and returns true if successful, false otherwise."
  [[i j]]
  (if (and (= (:current-player @game-state) :white)
           (<= 0 i board-size)
           (<= 0 j board-size)
           (nil? (get-in @game-state [:board j i])))
    (do
      (swap! game-state
             (fn [state]
               (-> state
                   (assoc-in [:board j i] :white)
                   (assoc :current-player :black))))
      (repaint!) ; TODO: Consider if this is smelly.
      true)
    false))

(defn white-pass
  "External function for white player to pass their turn.
   Returns true if it was white's turn and the pass was successful."
  []
  (if (= (:current-player @game-state) :white)
    (do
      (swap! game-state assoc :current-player :black)
       true)
    false))

(defn white-resign
  "External function for white player to resign.
   Returns the stone counts if it was white's turn and the resignation was
   successful, nil otherwise."
  []
  (if (= (:current-player @game-state) :white)
    (do
      (handle-resign))
    nil))


;;; ----------------------------------------------------------------------------
;;;   The UI and it's interactions.
;;;

;; Draw the board from definitions, with stone from game-state.
(defn draw-board [c g]
  (let [bg-color (color/color 220 179 92)]
    ;; Draw background
    (g/draw g
            (g/rect 0 0 total-size total-size)
            (g/style :background bg-color))

    ;; Draw grid lines
    (doseq [i (range (inc board-size))]
      (let [pos (+ board-margin (* i cell-size))]
        ;; Horizontal lines
        (g/draw g
                (g/line pos board-margin pos (+ board-margin (* board-size cell-size)))
                (g/style :foreground java.awt.Color/BLACK :stroke 2))
        ;; Vertical lines
        (g/draw g
                (g/line board-margin pos (+ board-margin (* board-size cell-size)) pos)
                (g/style :foreground java.awt.Color/BLACK :stroke 2))))

    ;; Draw stones
    (doseq [i (range (inc board-size))
            j (range (inc board-size))]
      (let [stone (get-in @game-state [:board j i])]
        (when stone
          (let [[x y] (get-intersection-position i j)
                stone-color (if (= stone :black) java.awt.Color/BLACK java.awt.Color/WHITE)]
            (g/draw g
                    (g/circle x y stone-radius)
                    (g/style :foreground java.awt.Color/BLACK
                             :background stone-color
                             :stroke 1.5))))))))

;; Create UI components
(defn create-panel []
  (let [panel (s/canvas :id :board
                        :background (color/color 220 179 92)
                        :paint draw-board
                        :preferred-size [total-size :by total-size])]

    ;; Add mouse listener for placing stones
    (s/listen panel :mouse-clicked
              (fn [e]
                (let [x (.getX e)
                      y (.getY e)]
                  (when-let [[i j] (get-board-position x y)]
                    (place-stone i j (:current-player @game-state))
                    (s/repaint! panel)))))
    panel))

(defn create-close-button []
  (s/button :text "Close"
            :listen [:action (fn [_]
                               (let [frames (s/all-frames)]
                                 (doseq [f frames]
                                   (when (= (.getTitle f) "Game of Go")
                                     (.dispose f)))))]))

(defn create-pass-button []
  (s/button :text "Pass"
            :listen [:action (fn [e]
                               (pass-turn)
                               ;; Get the component that triggered the event
                               (let [source (.getSource e)
                                     ;; Find the board canvas from the root container
                                     board (s/select (s/to-root source) [:#board])]
                                 ;; Repaint the board if found
                                 (when board
                                   (s/repaint! board))))]))

(defn create-resign-button []
  (s/button :text "Resign"
            :listen [:action (fn [_]
                               (handle-resign))]))

(defn create-frame []
  (let [panel (create-panel)
        close-button (create-close-button)
        pass-button (create-pass-button)
        resign-button (create-resign-button)
        ;; Create left panel for game control buttons
        left-buttons (s/horizontal-panel
                      :items [pass-button resign-button])
        ;; Create a panel with BorderLayout to position close button on right
        button-panel (s/border-panel
                      :west left-buttons
                      :east close-button)
        content-panel (s/border-panel
                       :center panel
                       :south button-panel)
        frame (s/frame :title "Game of Go"
                       :content content-panel
                       :on-close :dispose)]
    (-> frame s/pack! s/show!)))


(defn -main [& args]
  (create-frame))
