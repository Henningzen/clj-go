(ns no.jansenh.clj-go.board
  (:require [seesaw.color :as color]
            [seesaw.core :as s]
            [seesaw.graphics :as g]))

;; Define constants
(def board-size 8)
(def cell-size 50)
(def stone-radius 20)
(def board-margin 30)
(def total-size (+ (* board-size cell-size) (* 2 board-margin)))

;; Game state atom
(def game-state (atom {:board (vec (repeat (inc board-size)
                                           (vec (repeat (inc board-size) nil))))
                       :current-player :black}))

;; Utility functions
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

;; Drawing functions
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

;; Then modify your create-frame function to include this button:
(defn create-frame []
  (let [panel (create-panel)
        close-button (create-close-button)
        button-panel (s/horizontal-panel :items [close-button])
        content-panel (s/border-panel :center panel
                                      :south button-panel)
        frame (s/frame :title "Game of Go"
                       :content content-panel
                       :on-close :dispose)]
    (-> frame s/pack! s/show!)))


(defn -main [& args]
  ;; Either call directly instead of using invoke-later
  (create-frame)

  ;; Or if you want to keep using invoke-later, make sure it doesn't return immediately
  ;; by adding a blocking operation to keep the JVM alive
  #_(do
      (s/invoke-later create-frame)
      ;; Keep the JVM running
      @(promise)))

(comment
  (-main)
  ;;---> comment
  )

