/**
 * Created by Oleh Kurpiak on 3/29/2015.
 */

var loadFile = function(event) {
    var output = document.getElementById('cover');
    output.src = URL.createObjectURL(event.target.files[0]);
};