import static sys.*

static boolean drinkWater(boolean usebucket) {
  def inv = waitInventory()
  for (item in inv.getItems())
    if (isFlask(item)) {
        if (!item.isTooltip("Empty")) {
            drink(item)
            return true
        } else if (usebucket) {
            def bucket = findFilledBucket(inv)
            if (bucket != null) {
                int bx = bucket.row
                int by = bucket.column
                // take bucket
                bucket.take()
                waitDrag()
                // click on flask
                item.interact()
                sleep 500
                // put bucket back
                inv.drop(bx, by)
                waitDrop()
                // drink
                drink(item)
                return true
            }
        }
    }
  return false    
}

static void drink(item) {
    assert !item.isTooltip("Empty")
    item.act()
    waitContextMenu().select("Drink")
    waitHourglass()
}

static findFilledBucket(inv) {
    for (item in inv.getItems())
        if (item.isName("bucket-water") && item.isTooltip("water"))
            return item
}

static boolean isFlask(item) {
    return item.isName("waterflask") || item.isName("waterskin")
}