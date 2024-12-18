if (window.location.href == "http://localhost:8080/" || window.location.href == "http://localhost:8080/index.html") {
	window.onload = () => {
		$.ajax(
			{
				url: "http://localhost:8080/artist",
				type: 'GET',
				dataType: 'json',
				success: function (artistData) {
					generateArtistTable(artistData);
				},
				error: function (xhr, thrownError) {
					console.log(xhr.status);
					console.log(thrownError);
				}
			});
		$.ajax(
			{
				url: "http://localhost:8080/album",
				type: 'GET',
				dataType: 'json',
				success: function (albumData) {
					generateAlbumTable(albumData, true);
				},
				error: function (xhr, thrownError) {
					console.log(xhr.status);
					console.log(thrownError);
				}
			});
	};
}

if (window.location.href == "http://localhost:8080/addAlbum.html") {
	window.onload = () => {
		$.ajax(
			{
				url: "http://localhost:8080/artist",
				type: 'GET',
				dataType: 'json',
				success: function (artistData) {
					var artistList = [];
					for (let artist of artistData) {
						artistList.push(artist.name);
					}

					var sel = document.getElementById('artistList');
					for (var i = 0; i < artistList.length; i++) {
						var opt = document.createElement('option');
						opt.innerHTML = artistList[i];
						opt.value = artistList[i];
						sel.appendChild(opt);
					}
				},
				error: function (xhr, thrownError) {
					console.log(xhr.status);
					console.log(thrownError);
				}
			});
	};
}

function generateArtistTable(artistData) {
	const tableBody = document.getElementById('artistData');
	let dataHtml = '';

	for (let artist of artistData) {
		var genre = getGenre(artist.genre);
		dataHtml += `<tr data-artist-id="${artist.id}" class="artist-row"><td>${artist.name}</td><td>${genre}</td></tr>`;
	}

	tableBody.innerHTML = dataHtml;

	const artistRows = document.querySelectorAll('.artist-row');
	artistRows.forEach(row => {
		row.addEventListener('click', function () {
			const artistId = this.getAttribute('data-artist-id');
			window.location.href = `artistDetails.html?artistId=${artistId}`;
		});
	});
}

function generateAlbumTable(albumData, flag) {
	const tableBody = document.getElementById('albumData');
	let dataHtml = '';

	for (let album of albumData) {
		if (album.age > 0) {
			var releaseDate = new Date(album.releaseDate).toUTCString();
			releaseDate = releaseDate.split(' ').slice(0, 4).join(' ');
			releaseDate = releaseDate + " (" + album.age + " years ago)";
		}
		else {
			var releaseDate = new Date(album.releaseDate).toUTCString();
			releaseDate = releaseDate.split(' ').slice(0, 4).join(' ');
		}

		if (flag) {
			dataHtml += `<tr data-album-id="${album.id}" class="album-row"><td>${album.name}</td><td>${releaseDate}</td><td>${album.artist.name}</td></tr>`;
		} else {
			dataHtml += `<tr><td>${album.name}</td><td>${releaseDate}</td></tr>`;
		}
	}

	tableBody.innerHTML = dataHtml;

	const albumRows = document.querySelectorAll('.album-row');
	albumRows.forEach(row => {
		row.addEventListener('click', function () {
			const albumId = this.getAttribute('data-album-id');
			window.location.href = `albumDetails.html?albumId=${albumId}`;
		});
	});
}

function getGenre(genre) {
	if (genre == "ROCK") {
		return "Rock";
	}
	else if (genre == "POP") {
		return "Pop";
	}
	else if (genre == "HIP_HOP") {
		return "Hip-Hop";
	}
	else if (genre == "INDIE") {
		return "Indie";
	}
	else if (genre == "ELECTRONIC") {
		return "Electronic";
	}
	else if (genre == "SHOEGAZE") {
		return "Shoegaze";
	}
	else if (genre == "AMBIENT") {
		return "Ambient";
	}
	else if (genre == "METAL") {
		return "Metal";
	}
	else if (genre == "INDUSTRIAL") {
		return "Industrial";
	}
	else {
		return "";
	}
}

function validateArtist() {
	var artistName = document.forms["artistDetails"]["artistName"].value.replace(/\s+/g, ' ').trim()
	var genre = document.forms["artistDetails"]["genre"].value.trim();

	let artistData =
	{
		name: artistName,
		genre: genre
	};

	var genreFlag = false;
	var artistNameFlag = false;

	if (genre == "SELECT") {
		document.getElementById("genreError").innerHTML = "Please select a genre";
	}
	else {
		genreFlag = true;
		document.getElementById("genreError").innerHTML = "";
	}

	if (artistName == "") {
		document.getElementById("artistNameError").innerHTML = "Please enter the artist name";
	}
	else {
		artistNameFlag = true;
		document.getElementById("artistNameError").innerHTML = "";
	}

	if (artistNameFlag && genreFlag) {
		$.ajax(
			{
				headers:
				{
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				'type': 'POST',
				'url': "http://localhost:8080/artist",
				'data': JSON.stringify(artistData),
				'success': function () {
					window.location.href = "index.html";
				},
				error: function (xhr) {
					var errorMessage = JSON.parse(xhr.responseText).message;

					if (errorMessage.charAt(0) == 'V') {
						errorMessage = "Length of artist name should not exceed 30 characters";
					}
					document.getElementById("artistNameError").innerHTML = errorMessage;
				}
			});
	}
}

function validateAlbum() {
	var albumName = document.forms["albumDetails"]["albumName"].value.replace(/\s+/g, ' ').trim()
	var releaseDate = document.forms["albumDetails"]["releaseDate"].value.trim();
	var artist = document.forms["albumDetails"]["artistList"].value.trim();

	var artistFlag = false;
	var albumNameFlag = false;
	var releaseDateFlag = false;

	if (artist == "SELECT") {
		document.getElementById("artistError").innerHTML = "Please select an artist";
	}
	else {
		artistFlag = true;
		document.getElementById("artistError").innerHTML = "";
	}

	if (albumName == "") {
		document.getElementById("albumNameError").innerHTML = "Please enter the album name";
	}
	else {
		albumNameFlag = true;
		document.getElementById("albumNameError").innerHTML = "";
	}

	if (releaseDate == "") {
		document.getElementById("releaseDateError").innerHTML = "Please enter the release date";
	}
	else {
		releaseDateFlag = true;
		document.getElementById("releaseDateError").innerHTML = "";
	}

	var id = getArtistId(artist);

	let albumData =
	{
		name: albumName,
		releaseDate: releaseDate,
		artist:
		{
			id: id
		}
	};

	if (albumNameFlag && artistFlag && releaseDateFlag) {
		$.ajax(
			{
				headers:
				{
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				'type': 'POST',
				'url': "http://localhost:8080/album",
				'data': JSON.stringify(albumData),
				'success': function () {
					window.location.href = "index.html";
				},
				error: function (xhr) {
					var errorMessage = JSON.parse(xhr.responseText).message;

					if (errorMessage.charAt(0) == 'V') {
						errorMessage = "Length of album name should not exceed 50 characters";
					}
					document.getElementById("albumNameError").innerHTML = errorMessage;
				}
			});
	}
}

function getArtistId(artist) {
	var id;
	$.ajax(
		{
			url: "http://localhost:8080/artist/name/" + artist,
			type: 'GET',
			async: false,
			dataType: 'json',
			success: function (artistData) {
				id = artistData.id;
			},
			error: function (xhr, thrownError) {
				console.log(xhr.status);
				console.log(thrownError);
			}
		});
	return id;
}
