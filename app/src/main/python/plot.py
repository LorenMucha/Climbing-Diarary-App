import cloudscraper

def login():
    scraper = cloudscraper.create_scraper(delay=10,
                                          browser={
                                              'browser': 'chrome',
                                              'platform': 'android',
                                              'desktop': False
                                          })
    scraper.create_scraper()
    data = scraper.get('https://vlatka.vertical-life.info/auth/realms/Vertical-Life/protocol/openid-connect/auth?client_id=8a-nu&scope=openid%20email%20profile&response_type=code&redirect_uri=https%3A%2F%2Fwww.8a.nu%2Fcallback&resource=https%3A%2F%2Fwww.8a.nu&code_challenge=kLBNGJb5yjGCiSJsZ0nB1BETB6XLPijTADd_Uu4zDPM&code_challenge_method=S256')
    print(data.text)

login()
