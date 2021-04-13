mapboxgl.accessToken = 'pk.eyJ1IjoiamhhbjEzMTkiLCJhIjoiY2tuNzVwZDJ6MGt0azJubTEzNThoaW16MSJ9.66OOhUKKOgPJ8Pqw97IBtw';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [-70.6931,19.4655551],
    zoom: 13
});

var count = parseInt(localStorage.getItem("counter"));
var fm = [];
 fm.push(localStorage.getItem("forms"));
let fjson = JSON.parse(fm.toString());
 //console.log("ESTO ES FM "+ JSON.parse(fm.toString()));
let json;
if (fm != null) {
    for (let i = 0; i <= fm.length ; i++) {

        json = JSON.parse(JSON.stringify(fjson[i]))

        //console.log("ESTO ES EL JSON "+ JSON.parse(json))

        //console.log("ESTA ES LA LATITUD  " + JSON.parse(json)["latitud"]);

        marcadores(JSON.parse(json)["latitud"],JSON.parse(json)["longitud"])
        //console.log(JSON.parse(f)["latitud"])
    }
}

function marcadores(lat, long){

    var marker1 = new mapboxgl.Marker({color: 'black'})
        .setLngLat([long, lat])
        .addTo(map);
}