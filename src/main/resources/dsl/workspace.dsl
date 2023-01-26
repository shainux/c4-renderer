workspace {
    name = "McMakler IT Systems"
    description = "Represents the entire landscape of McMakler services and systems"
    model {
        !include external_systems.dsl
        mcm = enterprise "McMakler" {
            !include products/core/system.dsl
            !include products/web3.dsl
            !include products/mcenergieausweis.dsl
            !include products/mccompass.dsl
            group "Sales" {
                !include products/buyer_portal.dsl
                !include products/immoforce.dsl
            }
            group "Lead Acquisition"{
                !include products/brokerforce/system.dsl
                !include products/owner_lounge.dsl
                !include products/widget_x.dsl
            }
        }


        !include internal_people.dsl
        !include external_people.dsl
    }

    views {
        systemlandscape "SystemLandscape" {
                include *
                autoLayout lr
        }
        !include products/brokerforce/views.dsl

        styles {
            element "Software System" {
                shape RoundedBox
                background #1168bd
                color #ffffff
            }
            element "Person" {
                shape person
                background #08427b
                color #ffffff
            }
            element "external"{
                background #84858a
            }
            element "internal"{
            }
            relationship "Relationship" {
                routing direct
                width 150
            }
            relationship "pos10"{
                position 10
            }
            relationship "pos20"{
                position 20
            }
            relationship "pos30"{
                position 30
            }
            relationship "pos40"{
                position 40
            }
            relationship "pos50"{
                position 50
            }
            relationship "pos60"{
                position 60
            }
            relationship "pos70"{
                position 70
            }
            relationship "pos80"{
                position 80
            }
            relationship "pos90"{
                position 90
            }
        }
    }

}
