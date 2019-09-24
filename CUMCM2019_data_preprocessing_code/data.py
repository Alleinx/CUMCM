import csv
import preprocessing
import os

# get all files in the data dir
# --------------------------------
path = '/Users/user/Desktop/Data/'
files = os.listdir(path)

all_files = []

for file in files:
    if os.path.isfile(path + file):
        all_files.append(file[:8])

print(all_files)

# Start processing
# --------------------------------
for file_name in all_files:
    print('start processing file: ' + file_name + '.txt')
    data = []
    car_index = set()
    count = 0


    with open('/Users/user/Desktop/Data/' + file_name + '.txt') as fp:
        # Initialize csv file writer:

        csvfile = open('/Users/user/Desktop/result/' + file_name + '.csv', 'w', newline='')
        writer = csv.writer(csvfile)

        # Data preprocessing:
        # Too much data to fit into memory all at once, so we need to handle parts of the data
        # each time.
        for line in fp:
            single_data_point = fp.readline()
            
            # Handle empty string
            try:
                current_car_index = int(single_data_point.strip().split(',')[0])
            except ValueError as e:
                print(repr(e))
                continue
            
            # first data point
            if len(car_index) == 0:
                data.append(single_data_point)
                car_index.add(current_car_index)

            else:
                if current_car_index in car_index:
                    data.append(single_data_point)

                else:
                    # Operations on current data:
                    result = sorted(data, key=lambda item: item.strip().split(',')[-1].split(' '))
                    count += 1
                    if count % 200 == 0:
                        print('start processing #car:', count)

                    # Save processed data to csv file
                    preprocessing.save_data_to_csv_file(result, writer)

                    # Clear all processed data and add new data
                    car_index.add(current_car_index)
                    data.clear()
                    data.append(single_data_point)
        
        
        print('Finish processing file: ' + file_name + '.txt')