# Shopalyst2
Problem Definition 
Customer reviews from the amazon website under the beauty category is available as a JSON file. The task is to load this information into a database and analyze the information (elasticsearch is the preferred datastore, but you may also use another database)
Data set
The amazon reviews for beauty is available at this link. 
Sample row(s) from the file is below:
{"reviewerID": "A1YJEY40YUW4SE", "asin": "7806397051", "reviewerName": "Andrea", "helpful": [3, 4], "reviewText": "Very oily and creamy. Not at all what I expected... ordered this to try to highlight and contour and it just looked awful!!! Plus, took FOREVER to arrive.", "overall": 1.0, "summary": "Don't waste your money", "unixReviewTime": 1391040000, "reviewTime": "01 30, 2014"}

{"reviewerID": "A60XNB876KYML", "asin": "7806397051", "reviewerName": "Jessica H.", "helpful": [1, 1], "reviewText": "This palette was a decent price and I was looking for a few different shades. This palette conceals decently, however, it does somewhat cake up and crease.", "overall": 3.0, "summary": "OK Palette!", "unixReviewTime": 1397779200, "reviewTime": "04 18, 2014"}

Once the data is loaded into the database, please write a program (python, nodeJS or Java) to print the following output.
Total number of records
Top ten reviewers and their count of reviews
Top ten products which had reviews, their count of reviews and average rating
