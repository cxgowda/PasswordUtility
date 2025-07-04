from dhanhq import dhanhq


client_id       = ""
access_token    = ""
stock_id        = ""

dhan = dhanhq(client_id, access_token)



# Place an order for Equity Cash
order = dhan.place_order(security_id ='11915',
    exchange_segment=dhan.BSE,
    transaction_type=dhan.SELL,
    quantity=1,
    order_type=dhan.MARKET,
    product_type=dhan.INTRA,
    price=0)

print(order)

holding = dhan.get_holdings()
position = dhan.get_positions()


print(position)
print(holding)