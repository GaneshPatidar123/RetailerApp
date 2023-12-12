package com.example.paypointretailer.Model.Response.BillPayment

data class GetPayMode(
    var ShowCheckButton: Boolean? = false,
    var AggregatorId: Int? = 0,
    var PaymodeCollection: MutableList<PaymodeCollection>? = null,
    var isBBPS: Boolean? = false,
    var CALabel: String? = null,
    var OPT1: String? = null,
    var OPT2: String? = null,
    var CALabelText: String? = null,
    var CALabelDataType: String? = null,
    var CALabelRegex: String? = null,
    var OPT1Text: String? = null,
    var OPT1DataType: String? = null,
    var OPT1Regex: String? = null,
    var blr_pmt_amt_exactness: String? = null,
    var Billerinputparam: String? = null,

)
