(ns view.layout
   (:require [hiccup.page :as h]
             [hiccup.form :as form]   
             [model.model :as model]   
             [noir.session :as session]
             ))


(defn common [title & body]
  "Page template"
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content
            "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title title]
    (h/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
       "/stylesheets/style.css"  ) 
    ]
  
   [:body {:style "height:100%"}
     [:div.wrapper
       [:div.middle
        body]]]))


(defn navbar []
  "navbar"
  [:nav.navbar.navbar-default
  [:div.container-fluid
   [:div.navbar-header
    [:a.navbar-brand {:href "#"} "Healthy lifestyle"]
    ]
   [:ul.nav.navbar-nav
      [:li.active [:a {:href "/"}"Home" ]]
    
(when (not (nil? (session/get :user)))
 (list[:li [:a {:href "/new"}"Add recipe" ]]
  [:li [:a {:href "/myrecipes"} "My recipes" ]])
)
    ]
(if (not(nil?(session/get :user)))
(list [:ul.nav.navbar-nav.navbar-right
         [:li 
         (form/form-to  {:role "form" :id "search-form" :class "form-inline"}[:post "/"]
         (form/text-field {:class "form-control" :placeholder "search"} :search "")
         (form/submit-button  {:class "btn"} "search"))]
          [:li [:a {:href "/#"}
           [:span.glyphicon.glyphicon-user]
           (session/get :user)]]
          [:li [:a {:href "/logout"}
           [:span.glyphicon.glyphicon-log-out]
           "Logout"]
     ]
    ])
   

(list
        [:ul.nav.navbar-nav.navbar-right
          [:li 
          (form/form-to  {:role "form" :id "search-form" :class "form-inline"}[:post "/"]
          
                (form/text-field {:class "form-control" :placeholder "search"} :search "")    
          (form/submit-button {:class "btn" } 
              "Search"
                ))]
           [:li [:a {:href "/register"}
           [:span.glyphicon.glyphicon-user]
           "Sign up"]]
           [:li [:a {:href "/login"}
           [:span.glyphicon.glyphicon-log-in]
           "Login"]]
     ])
 )
   ]]
   
  )



(defn navbar-new []
  "new navbar"
  [:nav.navbar.navbar-default
    [:div.container-fluid
     [:div.navbar-header
       [:a.navbar-brand {:href "#"} "Healthy lifestyle"]
        ]
     [:ul.nav.navbar-nav
       [:li [:a {:href "/"}"Home" ]]
   
       [:li.active [:a {:href "/new"}"Add recipe" ]]
       [:li [:a {:href "/myrecipes"} "My recipes" ]]
    
    ]
    [:ul.nav.navbar-nav.navbar-right
         
       [:li [:a {:href "/#"}
           [:span.glyphicon.glyphicon-user]
           (session/get :user)]]
       [:li [:a {:href "/logout"}
           [:span.glyphicon.glyphicon-log-out]
           "Logout"]
     ]
    ]
    
   ]]
   
  )

(defn navbar-show-recipe []
  "show recipe navbar"
 [:nav.navbar.navbar-default
  [:div.container-fluid
   [:div.navbar-header
    [:a.navbar-brand {:href "#"} "Healthy lifestyle"]
    ]
   [:ul.nav.navbar-nav
     [:li.active [:a {:href "/"}"Home" ]]
    (when (not (nil? (session/get :user)))
(list[:li [:a {:href "/new"}"Add recipe" ]]
     [:li [:a {:href "/myrecipes"} "My recipes" ]])
    )
    ]
   (if
     (not(nil?(session/get :user)))
(list [:ul.nav.navbar-nav.navbar-right         
         [:li [:a {:href "/#"}
           [:span.glyphicon.glyphicon-user]
           (session/get :user)]]
          [:li [:a {:href "/logout"}
           [:span.glyphicon.glyphicon-log-out]
           "Logout"]
     ]
    ])
(list
     [:ul.nav.navbar-nav.navbar-right
         [:li [:a {:href "/register"}
           [:span.glyphicon.glyphicon-user]
           "Sign up"]]
         [:li [:a {:href "/login"}
           [:span.glyphicon.glyphicon-log-in]
           "Login"]]
     ])
    )
   ]]
   
  )

 

(defn navbar-my-recipes []
  " navbar recipes"
  [:nav.navbar.navbar-default
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href "#"} "Healthy lifestyle"]
         ]
       [:ul.nav.navbar-nav
         [:li [:a {:href "/"}"Home" ]]
    (when (not (nil? (session/get :user)))
     (list[:li [:a {:href "/new"}"Add recipe" ]]
          [:li.active [:a {:href "/myrecipes"} "My recipes" ]])
    )
       ]
   
       [:ul.nav.navbar-nav.navbar-right     
          [:li [:a {:href "/#"}
           [:span.glyphicon.glyphicon-user]
           (session/get :user)]]
           [:li [:a {:href "/logout"}
            [:span.glyphicon.glyphicon-log-out]
           "Logout"]
     ]
    ]
   ]]
  )



(defn footer []
  "footer"
  [:footer.fixed-footer
   [:div {:id "off-padding"}
    [:h4#footer-txt "Healhty lifestyle 2016"]
    ]
   ]
  )


