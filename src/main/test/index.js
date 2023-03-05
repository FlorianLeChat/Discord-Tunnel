const { response } = require( "express" );
const express = require( "express" );
const app = express();

app.use( ( _request, response, next ) =>
{
	response.header( "Access-Control-Allow-Origin", "*" );
	next();
} );

app.get( "/discord", ( _request, response ) =>
{
	console.log( "Discord" );

	fetch( "https://discord.com/api/v9/channels/771427562484138025/typing", {
		"headers": {
			"accept": "*/*",
			"accept-language": "fr,fr-FR;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
			"authorization": "NzQ2MDQzMTM3NDkwNjgxOTk2.GcdPEt.gmqAcIoFRp7OI7T4s2zguk2gaR1zEBcg8riEQA",
			"cache-control": "no-cache",
			"pragma": "no-cache",
			"sec-ch-ua": "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Microsoft Edge\";v=\"110\"",
			"sec-ch-ua-mobile": "?0",
			"sec-ch-ua-platform": "\"Windows\"",
			"sec-fetch-dest": "empty",
			"sec-fetch-mode": "cors",
			"sec-fetch-site": "same-origin",
			"x-debug-options": "bugReporterEnabled",
			"x-discord-locale": "fr",
			"x-super-properties": "eyJvcyI6IldpbmRvd3MiLCJicm93c2VyIjoiQ2hyb21lIiwiZGV2aWNlIjoiIiwic3lzdGVtX2xvY2FsZSI6ImZyIiwiYnJvd3Nlcl91c2VyX2FnZW50IjoiTW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzExMC4wLjAuMCBTYWZhcmkvNTM3LjM2IEVkZy8xMTAuMC4xNTg3LjYzIiwiYnJvd3Nlcl92ZXJzaW9uIjoiMTEwLjAuMC4wIiwib3NfdmVyc2lvbiI6IjEwIiwicmVmZXJyZXIiOiIiLCJyZWZlcnJpbmdfZG9tYWluIjoiIiwicmVmZXJyZXJfY3VycmVudCI6IiIsInJlZmVycmluZ19kb21haW5fY3VycmVudCI6IiIsInJlbGVhc2VfY2hhbm5lbCI6InN0YWJsZSIsImNsaWVudF9idWlsZF9udW1iZXIiOjE3ODU5MCwiY2xpZW50X2V2ZW50X3NvdXJjZSI6bnVsbCwiZGVzaWduX2lkIjowfQ==",
			"cookie": "__dcfduid=e7af7df06d0711eda899c76e7321c072; __sdcfduid=e7af7df16d0711eda899c76e7321c0720425c5311b0925973f3a661466070ab1e9132cfc68e7fd9145e0ad6315fbf5d5; __cfruid=86f6727e7ad64e36c8b5f3e13b785b2394226a69-1678033564; locale=fr; __cf_bm=CBmPTAnwXSbD1it4X90ybl5GlS__l9TWxcHf7wujLYE-1678033566-0-AaLIAGgqz8zc1Xn0yFh2uHE8nm6sgkvTeU8DYQPCPWLn8gFEMy0G1HOfHCnRKB9Wlp7KE7NF5YQ+FGH57U5Tbp4naD2nVlW8XQWRgNJLdQTPkzFeZ7CinjwWhA1fkPObl9RDwuSBkE5wL2n97gAEE8M=",
			"Referer": "https://discord.com/channels/401777889449607178/771427562484138025",
			"Referrer-Policy": "strict-origin-when-cross-origin"
		},
		"body": null,
		"method": "POST"
	} )
		.then( ( data ) =>
		{
			return response.sendStatus( data.status );
		} )
		.catch( ( _error ) =>
		{
			return response.sendStatus( 500 );
		} );
} );

app.listen( 3001 );