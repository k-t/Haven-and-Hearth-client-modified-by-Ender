import static sys.*

// get table window
def tablewnd = findWindow("Table")
if (tablewnd == null) {
    println "Where is mah table >:|"
    return
}

// find food inventory by it's size
def foodinv = tablewnd.inventories.find { it.rowCount == 6 && it.columnCount == 6 }
if (foodinv == null) {
    println "Wtf... empty table."
    return
}

// Feast!
tablewnd.findButton("Feast!").click()
waitCursor("eat")

for (item in foodinv.items) {
    item.take()
    waitHourglass()
}