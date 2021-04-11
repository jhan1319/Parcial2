mapboxgl.accessToken = 'pk.eyJ1IjoiamhhbjEzMTkiLCJhIjoiY2tuNzVwZDJ6MGt0azJubTEzNThoaW16MSJ9.66OOhUKKOgPJ8Pqw97IBtw';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [-70.6931,19.4655551],
    zoom: 13
});

var count = parseInt(localStorage.getItem("counter"));

if (count != null){
    for (let i = 0 ; i <= count ; i++){
        let f = localStorage.getItem("form #" + i)

        marcadores(JSON.parse(f)["latitud"],JSON.parse(f)["longitud"])
        //console.log(JSON.parse(f)["latitud"])
    }
}

function marcadores(lat, long){

    var marker1 = new mapboxgl.Marker({color: 'black'})
        .setLngLat([long, lat])
        .addTo(map);
}