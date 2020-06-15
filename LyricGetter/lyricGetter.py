'''
lyricGetter.py

Retrieves lyrics from Genius.com

syntax:
    lyricGetter.py -a <artist> -s <song> -v 

    -v indicates verbose output

    To get all of an artist's songs, omit -s arg
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
def getArtistLyrics(artist): 
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

                    songLyrics = scrapeSongLyrics(song['path'])

                    artistObj.songs.append(Song(songTitle, artist, songLyrics))

                    if verbose:
                        print('Got song ' + songTitle)

            currentPage = json['response']['next_page']
            
        else: 
            print('Bad return code from Genius API - /artists')

    print(str(len(artistObj.songs)) + ' song lyrics obtained')

    writeToFile(artistObj)

# get an artist's genius id
def getArtistId(artist): 
    searchUrl = baseUrl + '/search'
    params = {'q': artist}

    response = requests.get(searchUrl, params=params, headers=headers)
    json = response.json()

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
def getArtistSongLyrics(song, artist):
    rc = -1

    searchUrl = baseUrl + '/search'
    params = {'q': song + ' ' + artist}

    response = requests.get(searchUrl, params=params, headers=headers)
    json = response.json()
    
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
    os.mkdir(artistName)

    for song in artistObj.songs:
        songName = song.name.replace('/', '-')

        f = codecs.open(artistName + '/' + songName + '.txt', 'w', encoding='utf-8')
        f.write(song.lyrics)

# main
try:
    opts, args = getopt.getopt(sys.argv[1:], "a:s:v")
except getopt.GetoptError:
    print('lyricGetter.py -a <artist> -s <song> -v')
    sys.exit(2)

artist = ''
song = ''
verbose = False

clientAccessToken = 'UiDbZJ9gBn95_lsC-JaA7plY-aTWEjTzcLD8qQ8DPGFXSwmFcYPb0Sc_wGKPzGAU'
headers = {'Authorization': 'Bearer ' + clientAccessToken}

baseUrl = "http://api.genius.com"

for opt, arg in opts:
    if opt == '-h':
        print('lyricGetter.py -a <artist> -s <song> -v')
        sys.exit()
        
    elif opt == "-a":
        artist = arg
    
    elif opt == "-s":
        song = arg

    elif opt == "-v":
        verbose = True

startTime = time.time()

print('Starting lyrics collection process')

if artist != '' and song == '':
    getArtistLyrics(artist)

elif artist != '' and song != '':
    rc = getArtistSongLyrics(song, artist)

    if rc != 0:
        print("Couldn't find song on Genius")

print('Took ' + str(time.time() - startTime) + ' seconds')