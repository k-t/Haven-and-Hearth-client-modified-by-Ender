import static sys.*
import haven.UI

def inventory = waitInventory()
def cupboard = getInventory("Cupboard")

if (cupboard == null) {
    println "No cupboard opened!"
    return
}

// how much leaves we've put already
int already = cupboard.items.size()
int needed = 48 - already
if (needed > 0)
    // send transfer message needed amount of times
    (1..needed).each { cupboard.transferTo(1) }


