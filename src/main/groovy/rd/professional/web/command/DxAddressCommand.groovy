package rd.professional.web.command

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class DxAddressCommand {

    String dxNumber
    String dxExchange
}
