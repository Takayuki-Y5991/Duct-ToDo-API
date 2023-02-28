(ns todo-api.handler.todo
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [todo-api.client.todo :as todo]))

(defmethod ig/init-key ::fetchAll [_ {:keys [db]}]
  (fn [_]
    (let [result (todo/fetchAll db)]
      (println result)
      [::response/ok result])
    )
  )

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ params] :ataraxy/result}]
    (let [result (todo/create db params)
          id (:id (first result))]
      (let [todo (todo/fetchOne db id)] 
        [::response/created todo])
      )))

(defmethod ig/init-key ::fetchOne [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [result (todo/fetchOne db id)]
      [::response/ok result])))

(defmethod ig/init-key ::delete [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [_ (todo/delete db id)]
      [::response/no-content])))

(defmethod ig/init-key ::update [_ {:keys [db]}]
  (fn [{[_ id params] :ataraxy/result}]
    (let [result (todo/change db id params)]
      [::response/ok result])))

(defmethod ig/init-key ::changeStatus [_ {:keys [db]}]
  (fn [{[_ id params] :ataraxy/result}]
    (let [_ (todo/changeStatus db id params)
          result (todo/fetchOne db id)]
      [::response/ok result])))