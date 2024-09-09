import Link from "next/link";

import { Card, CardHeader, CardTitle, CardContent } from "@/app/_components/ui/card";

import { serverFetch } from "@/lib/server-utils";

interface IBudgetLinkProps {
  budgetId: string;
  budgetName: string;
}

const BudgetLink: React.FC<IBudgetLinkProps> = ({ budgetId, budgetName }) => {
  return (
    <Link href={`/budget/${budgetId}`}>
      <Card>
        <CardHeader>
          <CardTitle>{budgetName}</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Some description</p>
        </CardContent>
      </Card>
    </Link>
  );
}

const CreateBudget: React.FC = () => {
  return (
    <Link href="/budget-create">
      <Card>
        <CardHeader>
          <CardTitle>Create Budget</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Click here to create a budget</p>
        </CardContent>
      </Card>
    </Link>
  );
}

const Page: React.FC = async () => {
  // FIXME: HANDLE ERRORS
  const response = await serverFetch("/api/getBudgetList");
  const budgets = response.ok ? await response.json() as (IBudgetLinkProps[] | null) : null;

  return (
    <div className="p-4">
      <h1 className="text-4xl">Your Budgets</h1>
      <main className="py-4 flex flex-row flex-wrap gap-4">
        {
          budgets?.map(budget => <BudgetLink key={budget.budgetId} budgetId={budget.budgetId} budgetName={budget.budgetName} />)
        }
        <CreateBudget />
      </main>
    </div>
  );
}

export default Page;

