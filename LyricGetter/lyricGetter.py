'''
lyricGetter.py

Retrieves lyrics from Genius.com

syntax:
    lyricGetter.py -a <artist> -i <artist_id> -s <song> -v -d

    -v indicates verbose output

    -d indicates debug output

    To get all of an artist's songs, omit -s arg

    To get an artist's id: 
        - Go to the artist's page on genius.com
        - View page html source
        - Search for "artist id"
        - The first result should look something like:
            {&quot;key&quot;:&quot;Artist ID&quot;,&quot;value&quot;:126}
        - In the above example, the number at the end, 126, is the id
'''

import requests
from bs4 import BeautifulSoup
import os
import time
import sys
import getopt
import codecs

# class used to hold song information
class Song:
    name = ''
    artist = ''
    lyrics = ''

    def __init__(self, name, artist, lyrics):
        self.name = name
        self.artist = artist
        self.lyrics = lyrics

# class used to hold information about an artist
class Artist:
    name = ''
    songs = []

    def __init__(self, name):
        self.name = name

# get all of an artist's songs' lyrics
def getArtistLyrics(artist, artistIdFlag): 
    if artistIdFlag:
        artistId = artist
        artist = getArtist(artistId)

        if artist == '':
            print("Couldn't find artist from id on Genius")
            return

    else:
        artistId = getArtistId(artist)

        if artistId == -1: 
            print("Couldn't find artist on Genius")
            return
    
    artistObj = Artist(artist)

    currentPage = 1

    while currentPage != None: 
        artistUrl = baseUrl + '/artists/' + str(artistId) + '/songs'
        params = {'per_page': 50, 'page': currentPage}

        response = requests.get(artistUrl, params=params, headers=headers)
        json = response.json()

        if json['meta']['status'] == 200:
            for song in json['response']['songs']:
                # get song if specified artist is primary artist on song and lyrics are complete
                if song['primary_artist']['name'].lower() == artist.lower() and song['lyrics_state'] == 'complete':
                    songTitle = song['title']
                    artistName = song['primary_artist']['name']
                    songLyrics = scrapeSongLyrics(song['path'])

                    artistObj.songs.append(Song(songTitle, artistName, songLyrics))

                    if verbose:
                        print('Got lyrics for ' + songTitle + ' by ' + artistName)

            currentPage = json['response']['next_page']
            
        else: 
            print('Bad return code from Genius API - /artists')

    print(str(len(artistObj.songs)) + ' by  ' + artist + ' lyrics obtained')

    writeToFile(artistObj)

# get an artist from id
def getArtist(artistId):
    artistUrl = baseUrl + '/artists/' + artistId

    response = requests.get(artistUrl, headers=headers)
    json = response.json()

    if debug:
        print(artistUrl + ' results: \n' + str(json))

    artistName = ''

    if json['meta']['status'] == 200:
        artistName = json['response']['artist']['name']

        if debug:
            print('Got artist from id: ' + artistName)

    else: 
        print('Bad return code from Genius API - /artist')

    return artistName

# get an artist's genius id
def getArtistId(artist): 
    searchUrl = baseUrl + '/search'
    params = {'q': artist}

    response = requests.get(searchUrl, params=params, headers=headers)
    json = response.json()

    if debug:
        print(searchUrl + str(params) + ' results: \n' + str(json))

    if json['meta']['status'] == 200:
        artistId = -1

        for hit in json['response']['hits']:
            artistObj = hit['result']['primary_artist']

            if artistObj['name'].lower() == artist.lower():
                artistId = artistObj['id']
                break
    else: 
        print('Bad return code from Genius API - /search')

    return artistId

# get a single song's lyrics by artist
def getArtistSongLyrics(song, artist, artistIdFlag):
    rc = -1

    if artistIdFlag:
        artist = getArtist(artist)

        if artist == '':
            print("Couldn't find artist from id on Genius")
            return

    searchUrl = baseUrl + '/search'
    params = {'q': song + ' ' + artist}

    response = requests.get(searchUrl, params=params, headers=headers)
    json = response.json()
    
    if debug:
        print(searchUrl + str(params) + ' results: \n' + str(json))

    if json['meta']['status'] == 200:
        for hit in json['response']['hits']:
            if  hit['type'] == 'song' and \
                hit['result']['primary_artist']['name'].lower() == artist.lower() and \
                hit['result']['title'].lower() == song.lower() and \
                hit['result']['lyrics_state'] == 'complete':

                artistObj = Artist(artist)

                songLyrics = scrapeSongLyrics(hit['result']['path'])

                artistObj.songs.append(Song(hit['result']['title'], artist, songLyrics))

                writeToFile(artistObj)

                if verbose:
                    print('Got lyrics for ' + hit['result']['title'] + ' by ' + artist)

                # mark good return code
                rc = 0
                break

    else: 
        print('Bad return code from Genius API - /search')

    return rc

# scrape song lyrics from webpage
def scrapeSongLyrics(lyricsPath):
        lyricsUrl = "http://genius.com" + lyricsPath

        response = requests.get(lyricsUrl)

        html = BeautifulSoup(response.text, "html.parser")

        # remove script tags that they put in the middle of the lyrics
        [h.extract() for h in html('script')]

        # get lyrics div element
        lyrics = html.find('div', class_='lyrics').get_text()

        return lyrics

# write results to new directory 
def writeToFile(artistObj): 
    artistName = artistObj.name.replace('/', '-')

    # create new directory using artist name
    try:
        os.mkdir(artistName)
    except:
        # dir already exists
        pass

    for song in artistObj.songs:
        songName = song.name.replace('/', '-')

        try:
            f = codecs.open(artistName + '/' + songName + '.txt', 'w', encoding='utf-8')
            f.write(song.lyrics)
        except:
            # file already exists?
            pass

# main
try:
    opts, args = getopt.getopt(sys.argv[1:], "a:s:vdi:")
except getopt.GetoptError:
    print('lyricGetter.py -a <artist> -i <artist_id> -s <song> -v -d')
    sys.exit(2)

artist = ''
artistIdArg = ''
song = ''
verbose = False
debug = False

clientAccessToken = 'UiDbZJ9gBn95_lsC-JaA7plY-aTWEjTzcLD8qQ8DPGFXSwmFcYPb0Sc_wGKPzGAU'
headers = {'Authorization': 'Bearer ' + clientAccessToken}

baseUrl = "http://api.genius.com"

for opt, arg in opts:
    if opt == '-h':
        print('lyricGetter.py -a <artist> -i <artist-id> -s <song> -v -d')
        sys.exit()
        
    elif opt == "-a":
        artist = arg
    
    elif opt == "-i":
        artistIdArg = arg

    elif opt == "-s":
        song = arg

    elif opt == "-v":
        verbose = True

    elif opt == "-d":
        debug = True

startTime = time.time()

print('Starting lyrics collection process')

if (artistIdArg != '' or artist != '') and song == '':
    getArtistLyrics(artistIdArg if artistIdArg != '' else artist, True if artistIdArg != '' else False)

elif (artistIdArg != '' or artist != '') and song != '':
    rc = getArtistSongLyrics(song, artistIdArg if artistIdArg != '' else artist, True if artistIdArg != '' else False)

    if rc != 0:
        print("Couldn't find song on Genius")

print('Took ' + str(time.time() - startTime) + ' seconds')