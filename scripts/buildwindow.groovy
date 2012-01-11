/* Demonstrates work with build windows. */

import static sys.*

def box = findWindow('Cupboard')?.findBuildBox('Board')
if (box == null) {
    println ":("
    return
}

def printbox = {
    println "Board: ${box.remaining}/${box.available}/${box.built}"
}

box.click()     // get board
waitDrag()
box.drop()      // put it back
waitDrop()
printbox()

waitInventory() // wait for opened inventory
(1..3).each { box.transferFrom() } // get 2 boards
(1..2).each { box.transferTo(1) }  // put back 2 boards (with shift key)
Thread.sleep 400 // wait for transfer
printbox()

findWindow('Cupboard')?.findButton("Build")?.click() // start building
waitHourglass()
printbox()