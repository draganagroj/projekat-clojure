(ns view.login
  (:require [hiccup.page :as h]
            [hiccup.form :as form]
             [ring.util.anti-forgery :as anti-forgery]
             [noir.session :as session] 
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST ]]
             [noir.response :refer [redirect]]
  ))

  
  (defn login-page
    "login page"
 [message]  
  (layout/common "Logging"
[:div.col-md-offset-3.col-md-6 
  [:div {:class "border-div"}
    [:h2.col-md-offset-4 {:id "reg-title"} "Login form"]
(form/form-to  {:role "form" :id "login-form" :class "form-horizontal"}[:post "/login"]
[:div {:class "form-group"}
(form/label {:class "reg-label control-label col-md-2"} "name" "username:")
[:div.col-md-9
(form/text-field  {:class "form-control"  :required "required"} :name "")]]
[:br]
[:div {:class "form-group"}
 (form/label {:class "reg-label control-label col-md-2"} "pass" "password:")
 [:div.col-md-9
(form/password-field  {:class "form-control"  :required "required"} :pass )]]
[:br]
(form/submit-button {:class "btn btn-info col-md-offset-5"} "login"))]
 (when (= message "Login error" )
 [:div.alert.alert-danger message]
   )
 ]
  
  )
   
    )
(defn login-check [name pass]
  "checking whether user is registered"
     (if (not (empty? (model/user name pass) ))
           (do (session/put! :user name)
                
               (redirect "/"))
           
           (login-page "Login error"))
           )         
   
  
(defroutes login
  "login routes"
    (GET "/login" [] (login-page ""))
    (GET "/logout" [] 
         (do
         (session/clear!)
         (redirect "/")))
  (POST "/login" [name pass]        
          (if (= true (clojure.string/blank? name))
             (login-page "Login error")
            (login-check name pass)
            )         
          ) 
 )
