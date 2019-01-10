package rd.professional.web.dto

import rd.professional.domain.DxAddress

class DxAddressDto {

    String dxNumber
    String dxExchange

    def DxAddressDto(DxAddress dxAddress) {
        dxNumber = dxAddress.dxNumber
        dxExchange = dxAddress.dxExchange
    }
}
