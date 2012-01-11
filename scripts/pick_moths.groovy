import static sys.*

def cupboard = getInventory("Cupboard")
if (cupboard != null) {
    def gender = Gender.Male // gender to start
    // starting position to put things 
    def sx = 0 
    def sy = 0
    // open inventory
    def inv = waitInventory()
    assert inv != null
    // if there are some items in the inventory
    def items = inv.getItems()
    if (items.size() > 0) {
        // if last item has gender, change gender of the next item to pick
        def item = items[items.length - 1]
        gender = changeGender(getItemGender(item))
        if (gender == Gender.Unknown)
            gender = Gender.Male
        // change start position
        sx = item.row
        sy = item.column + 1
    }
    
    for (x in sx..<inv.rowCount) {
        for (y in sy..<inv.columnCount) {
            def f = { i -> i.isTooltip("Silkmoth") && getItemGender(i) == gender }
            def moth = getBestItem(cupboard, f)
            if (moth != null) {
                moth.take()
                waitDrag()
                inv.drop(x, y)
                waitDrop()
                gender = changeGender(gender)
            } else {
              // interrupt script if there are no more moths with needed gender
              return
            }
      }
      sy = 0 // to be sure to start next row with first column
    }
} else {
    println "No cupboard opened"
}

enum Gender { Unknown, Male, Female }

// util functions

Gender changeGender(g) {
    if (g == Gender.Male) Gender.Female
    else if (g == Gender.Female) Gender.Male
    else g
}

Gender getItemGender(item) {
    if (item.isTooltip("Male")) Gender.Male
    else if (item.isTooltip("Female")) Gender.Female
    else Gender.Unknown
}

def getBestItem(container, filter) {
    return container.getItems()
        .findAll { filter(it) }
        .max { it.quality }
}

