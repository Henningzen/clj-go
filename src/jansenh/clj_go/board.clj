;;; src/jansenh/clj-go/board.clj  ---  Go game graphical board

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

(ns jansenh.clj-go.board
  (:require [seesaw.color :as color]
            [seesaw.core :as s]
            [seesaw.graphics :as g])
  (:import [java.awt Color]))
;;
;;  Clj-go board
;;  ------------
;;
;;    Clojure Go Game board implementation, using Seesaw clojure wrapper
;;    for Java Swing AWT graphics.
;;
;;    The Board has a basic state with:
;;      - black and white stones on a grid,
;;      - current player :black or :white,
;;      - events capturing mouse interaction from placing stones on the
;;        board and clicking the game buttons.
;;
;;    We got buttons for:
;;      1. pass turn
;;      2. resign
;;      3. close
;;
;;    The board has an API for interacting with the game programatically,
;;    currently supporting player :white.
;;
;;    The API support
;;      - :white add stone to the board, return true/false.
;;      - :white pass move.
;;
;; authors:   Henning Jansen, henning.jansen@jansenh.no
;; since:     0.1.1-SNAPSHOT  2025-04-17
;; version:   0.1.2           2026-05-13
;;

;; ------------------------------------------------------------------------------
;;  Definitions

(def board-size 9)          ; intersections define grid intersections
(def cell-size 30)           ; Adjusted cell size for a 19x19 board
(def stone-radius 13)        ; Adjusted stone radius for a 19x19 board
(def board-margin 20)        ; Adjusted board margin for a 19x19 board
(def total-size (+ (* board-size cell-size) (* 2 board-margin)))

;; ------------------------------------------------------------------------------
;;  State Management

(def board-state (atom {:board (vec (repeat board-size
                                            (vec (repeat board-size nil))))
                        :current-player :black}))

;; ------------------------------------------------------------------------------
;;  Utility Functions

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
    (when (and (<= 0 i (dec board-size)) (<= 0 j (dec board-size)))
      [i j])))

(defn place-stone
  "Place a stone on the board if the position is valid"
  [i j player]
  (when (and (<= 0 i (dec board-size))
             (<= 0 j (dec board-size))
             (nil? (get-in @board-state [:board j i])))
    (swap! board-state
           (fn [state]
             (-> state
                 (assoc-in [:board j i] player)
                 (assoc :current-player (if (= player :black) :white :black)))))))

(defn pass-turn
  "Pass the current player's turn"
  []
  (swap! board-state update :current-player
         (fn [current-player]
           (if (= current-player :black) :white :black))))

(defn count-stones
  "Count the number of black and white stones on the board"
  []
  (let [board (:board @board-state)
        stones (for [i (range board-size)
                     j (range board-size)
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

;; ------------------------------------------------------------------------------
;;  ML Player - Automata using Machine Learning

(defn make-white-move
  "External function for placing a white stone on the board.
   Function has side-effect with a repaint! on the JavaFX external libraries.
   Takes board coordinates [i j] and returns true if successful, false otherwise."
  [[i j]]
  (if (and (= (:current-player @board-state) :white)
           (<= 0 i (dec board-size))
           (<= 0 j (dec board-size))
           (nil? (get-in @board-state [:board j i])))
    (do
      (swap! board-state
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
  (if (= (:current-player @board-state) :white)
    (do
      (swap! board-state assoc :current-player :black)
      true)
    false))

(defn white-resign
  "External function for white player to resign.
   Returns the stone counts if it was white's turn and the resignation was
   successful, nil otherwise."
  []
  (if (= (:current-player @board-state) :white)
    (do
      (handle-resign))
    nil))

;; ------------------------------------------------------------------------------
;;  The UI and Its Interactions

(defn draw-board [c g]
  (let [bg-color (color/color 220 179 92)]
    ;; Draw background
    (g/draw g
            (g/rect 0 0 total-size total-size)
            (g/style :background bg-color))

    ;; Draw grid lines
    (doseq [i (range board-size)]
      (let [pos (+ board-margin (* i cell-size))]
        ;; Horizontal lines
        (g/draw g
                (g/line pos board-margin pos (+ board-margin (* (dec board-size) cell-size)))
                (g/style :foreground Color/BLACK :stroke 2))
        ;; Vertical lines
        (g/draw g
                (g/line board-margin pos (+ board-margin (* (dec board-size) cell-size)) pos)
                (g/style :foreground Color/BLACK :stroke 2))))

    ;; Draw stones
    (doseq [i (range board-size)
            j (range board-size)]
      (let [stone (get-in @board-state [:board j i])]
        (when stone
          (let [[x y] (get-intersection-position i j)
                stone-color (if (= stone :black) Color/BLACK Color/WHITE)]
            (g/draw g
                    (g/circle x y stone-radius)
                    (g/style :foreground Color/BLACK
                             :background stone-color
                             :stroke 1.5))))))))

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
                    (place-stone i j (:current-player @board-state))
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

(defn create-frame
  "Main entry point for clj-go Board."
  [& arg]
  (let [panel (create-panel)
        close-button (create-close-button)
        pass-button (create-pass-button)
        resign-button (create-resign-button)

        left-buttons (s/horizontal-panel
                      :items [pass-button resign-button])

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
