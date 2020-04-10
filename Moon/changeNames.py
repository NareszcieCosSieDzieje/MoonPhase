import glob
import re
import os

# Function to rename multiple files 
def main():   


	jpgs = []
	for file in glob.glob("*.jpg"):
		jpgs.append(file)


	for filename in jpgs:
	
		x = re.search("[0-9]+\.*[0-9]+", filename[1:])
		
		if x is not None:
			dst = filename[0].lower() + x.string 
			dst = dst[0:-4]
			dst = dst.replace('p','')
			dst = dst.replace('.','_')
			dst = dst.replace('-','_')
			dst += ".png"
			src = filename
			print(dst)
			os.rename(filename, dst) 


# Driver Code 
if __name__ == '__main__': 
	  
	# Calling main() function 
	main() 