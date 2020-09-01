package id.adeds.dynamic_ui.util


fun String?.isValid() = this != null && this.isNotEmpty() && !this.equals("null", true)
