import csv

def save_data_to_csv_file(input_data, writer):
    data = [line.strip().split(',') for line in input_data]
    flag = 0

    # temporarily store client in/out information.
    temp = []
    result = []
    index = 1

    for item in data:
        if flag == 0 and int(item[3]) == 0:
            # No client
            continue
        elif flag == 0 and int(item[3]) == 1:
            # Client aboard the taxi
            temp.append((item[0], 
                            index, 
                            'start', 
                            float(item[1]), 
                            float(item[2]), 
                            int(item[-2]), 
                            item[-1].split(' ')[0], 
                            item[-1].split(' ')[1]))

            flag = 1
        elif flag == 1 and int(item[3]) == 1:
            # Taxi has loaded a client and running on the road.
            continue
        elif flag == 1 and int(item[3]) == 0:
            # Reachs the destination.
            temp.append((item[0], 
                            index, 
                            'end', 
                            float(item[1]), 
                            float(item[2]), 
                            int(item[-2]), 
                            item[-1].split(' ')[0], 
                            item[-1].split(' ')[1]))

            if len(temp) == 2:
                result.append([temp[0], temp[1]])
            else:
                raise ValueError('Unmatched number in temp list.')

            index += 1
            flag = 0
            temp.clear()

    for item in result:
        _csv_listwrite_function(writer, item)


def _csv_listwrite_function(writer, data): 
    for i in range(len(data)):
        writer.writerow(data[i])