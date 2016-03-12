(ns view.show-recipe
  (:require [hiccup.page :as h]
            [hiccup.form :as form]
             [noir.session :as session]
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST routes]]
             [noir.validation :refer [has-value?]]
             
   )
  )


(defn recipe-display [id]
  "display recipe"
  [:div.row
    [:div.col-md-offset-3.col-md-8   
     [:div.col-md-9 {:id "txt-area-2" } 
       [:h2.col-md-offset-4.title (:title(model/recipe-details id))]
         [:h4.user.col-md-offset-9 "By "(:username(model/recipe-details id)) ]
         [:br]
          [:div 
           [:pre {:id "recipe"}
           (:body(model/recipe-details id))
           ]]
       ]]
  ]
  )


(defn like-dis [id]
  "display like and dislike buttons"
 [:div.row
   [:div#like-div
   (form/form-to  {:role "form" :class "form-btn"} [:post (str "/show/" id) ] 
                (form/text-field {:style "display:none"} "like" "like")
                  (form/submit-button {:class "btn btn-default" :id "like"} "" )
                  )
   (form/form-to  {:role "form" :class "form-btn" } [:post (str "/show/" id) ]
                 (form/text-field {:style "display:none"} "dislike" "dislike")
                  (form/submit-button {:class "btn btn-default" :id "dislike"} "" )
                  )
   ]]
  )


(defn comment-form [id]
  "display comment form"
  [:div.row 
   [:div.col-md-offset-2.col-md-8.#comment-form
   (form/form-to  {:role "form" :class "form-btn"}[:post (str "/show/" id) ]
                  [:div {:class "form-group"}
                     (form/label {:class "reg-label control-label col-md-2"} "c" "leave comment")
                  [:div.col-md-9
                    (form/text-field {:class "form-control" :id "text-field" } "comment" "" )]
                  [:br]
                  [:br]
                    ]
                    (form/submit-button {:class "btn btn-default col-md-offset-5" } "publish")
                  )
    ]
   ]
  
  )

(defn comments [id]
  "display comments for recipe"
 (for [com (model/get-comments id)]
   [:div.row
     [:div.col-md-offset-3.col-md-6 {:class "comments"}
       [:div {:class "form-group"}
                     (form/label {:class "reg-label control-label col-md-3 col-md-offset-9" :id "font-labe"} "username" (str "user "(:username com)))
                    [:br]
                    [:div.col-md-12
                     (form/text-field {:class "form-control " :id "comment-field" :disabled "true"} "com" (:text com) )]
                    [:br]]
      ]
    ]
   )
  )

(defn recommended-titles [id]
  "titles of recommended recipes"
  (into [] (filter  #(not(= (:title(model/recipe-details id)) %) ) (model/return-titles (session/get :user)) )
  ) 
  )

(defn get-id [title]
  "making a link to recipe"
  (str "/show/"(model/recipe-id title))
  )

(defn recomendation [id]
  "displaying recommended links"
 [:div.col-md-offset-3.col-md-6#recommended-div-1
  [:p.col-md-offset-4#recommended-div-2 "Recommended recipes"]
 (for [title (recommended-titles id)]
   [:div.row 
     [:a {:href (get-id title) :style "margin-left:5%"} title]]
   )
 ]
  )

(defn show-selected
  "show-recipe page"
  [id]
   (layout/common "Recipe"
                  (layout/navbar-show-recipe)
                  (recipe-display id)
                  
                  (when (not(nil?(session/get :user)))
                    (like-dis id))
                  
                  (when (not(nil?(session/get :user)))
                    (recomendation id)) 
                    
                  (when (not(nil?(session/get :user)))
                   (comment-form id))
                  
                 (comments id)
                 (layout/footer)
           )
  
  )


(defroutes show
  "show recipe routes"
 (GET "/show/:id" [id] (show-selected id ))
 (POST "/show/:id" [ comment id like dislike] (cond
                                                (not(nil? comment )) (do
                                                                     (model/insert-commment comment (session/get :user) id)
                                                                     (show-selected id))
                                                (not(nil? like))     (do 
                                                                      (model/update-rating 1 (session/get :user) id)
                                                                      (recommended-titles id)
                                                                      (show-selected id))
                                     
                                              (not(nil? dislike ))   (do
                                                                      (model/update-rating -1 (session/get :user) id)
                                                                      (recommended-titles id)
                                                                      (show-selected id))
                                        )
  ))