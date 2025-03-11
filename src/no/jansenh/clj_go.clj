(ns no.jansenh.clj-go
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main [& args]
  ;; Either call directly instead of using invoke-later
  (no.jansenh.clj-go.board/create-frame))

(comment
  (-main)
  ;;---> comment
  )
