workspace {
    name = "Test IT Systems"
    description = "Represents the entire landscape of services and systems"
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



}
